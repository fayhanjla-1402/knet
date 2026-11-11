DROP DATABASE IF EXISTS db_knet;
CREATE DATABASE db_knet;
USE db_knet;

CREATE TABLE member (
    idMember INT AUTO_INCREMENT PRIMARY KEY,
    nama VARCHAR(50) NOT NULL,
    noHp VARCHAR(15),
    tanggalDaftar DATE NOT NULL
);

CREATE TABLE komputer (
    idKomputer INT AUTO_INCREMENT PRIMARY KEY,
    namaKomputer VARCHAR(30) NOT NULL UNIQUE,
    tarifPerJam INT NOT NULL,
    status VARCHAR(10) NOT NULL DEFAULT 'Tersedia'
);

CREATE TABLE sesi (
    idSesi INT AUTO_INCREMENT PRIMARY KEY,
    idMember INT NOT NULL,
    idKomputer INT NOT NULL,
    jamMulai DATETIME NOT NULL,
    jamSelesai DATETIME,
    totalBiaya INT NOT NULL DEFAULT 0,
    FOREIGN KEY (idMember) REFERENCES member(idMember),
    FOREIGN KEY (idKomputer) REFERENCES komputer(idKomputer)
);

INSERT INTO member (nama, noHp, tanggalDaftar) VALUES
('Andi', '081234567801', '2026-11-01'),
('Budi', '081234567802', '2026-11-02'),
('Citra', '081234567803', '2026-11-03');

INSERT INTO komputer (namaKomputer, tarifPerJam, status) VALUES
('PC-01', 5000, 'Tersedia'),
('PC-02', 5000, 'Tersedia'),
('PC-03', 7000, 'Tersedia'),
('PC-04', 7000, 'Tersedia');

CREATE VIEW vRiwayatSesi AS
SELECT
    sesi.idSesi,
    sesi.idMember,
    member.nama AS namaMember,
    sesi.idKomputer,
    komputer.namaKomputer,
    sesi.jamMulai,
    sesi.jamSelesai,
    sesi.totalBiaya
FROM sesi
JOIN member ON sesi.idMember = member.idMember
JOIN komputer ON sesi.idKomputer = komputer.idKomputer;

DELIMITER $$

-- Mencegah sesi baru dimulai di komputer yang masih Dipakai
CREATE TRIGGER trgSesiCekKomputer
BEFORE INSERT ON sesi
FOR EACH ROW
BEGIN
    IF NEW.jamSelesai IS NULL
       AND (SELECT status FROM komputer WHERE idKomputer = NEW.idKomputer) <> 'Tersedia' THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Komputer sedang dipakai';
    END IF;
END$$

-- Setelah sesi ditambah, status komputer menjadi Dipakai
CREATE TRIGGER trgSesiMulai
AFTER INSERT ON sesi
FOR EACH ROW
BEGIN
    IF NEW.jamSelesai IS NULL THEN
        UPDATE komputer SET status = 'Dipakai' WHERE idKomputer = NEW.idKomputer;
    END IF;
END$$

-- Setelah jamSelesai terisi, status komputer menjadi Tersedia
CREATE TRIGGER trgSesiSelesai
AFTER UPDATE ON sesi
FOR EACH ROW
BEGIN
    IF OLD.jamSelesai IS NULL AND NEW.jamSelesai IS NOT NULL THEN
        UPDATE komputer SET status = 'Tersedia' WHERE idKomputer = NEW.idKomputer;
    END IF;
END$$

-- Mengisi jamSelesai dan menghitung totalBiaya (dibulatkan ke atas per jam, minimal 1 jam).
-- p_totalBiaya = -1 jika sesi tidak ditemukan atau sudah selesai.
CREATE PROCEDURE spAkhiriSesi(IN p_idSesi INT, OUT p_totalBiaya INT)
BEGIN
    DECLARE v_mulai DATETIME DEFAULT NULL;
    DECLARE v_tarif INT DEFAULT 0;
    DECLARE v_selesai DATETIME;
    DECLARE v_jam INT;

    SELECT sesi.jamMulai, komputer.tarifPerJam
    INTO v_mulai, v_tarif
    FROM sesi
    JOIN komputer ON sesi.idKomputer = komputer.idKomputer
    WHERE sesi.idSesi = p_idSesi AND sesi.jamSelesai IS NULL;

    IF v_mulai IS NULL THEN
        SET p_totalBiaya = -1;
    ELSE
        SET v_selesai = NOW();
        SET v_jam = GREATEST(1, CEIL(TIMESTAMPDIFF(MINUTE, v_mulai, v_selesai) / 60));
        SET p_totalBiaya = v_jam * v_tarif;

        UPDATE sesi
        SET jamSelesai = v_selesai, totalBiaya = p_totalBiaya
        WHERE idSesi = p_idSesi;
    END IF;
END$$

DELIMITER ;

-- Data awal sesi (dimasukkan setelah trigger dibuat agar status komputer ikut terisi otomatis)
INSERT INTO sesi (idMember, idKomputer, jamMulai, jamSelesai, totalBiaya) VALUES
(1, 1, '2026-11-09 10:00:00', '2026-11-09 12:00:00', 10000),
(2, 2, '2026-11-09 13:00:00', '2026-11-09 14:30:00', 10000);

INSERT INTO sesi (idMember, idKomputer, jamMulai) VALUES
(3, 3, '2026-11-10 09:00:00');
