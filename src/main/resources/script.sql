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
('PC-03', 7000, 'Dipakai'),
('PC-04', 7000, 'Tersedia');

INSERT INTO sesi (idMember, idKomputer, jamMulai, jamSelesai, totalBiaya) VALUES
(1, 1, '2026-11-09 10:00:00', '2026-11-09 12:00:00', 10000),
(2, 2, '2026-11-09 13:00:00', '2026-11-09 14:30:00', 10000);

INSERT INTO sesi (idMember, idKomputer, jamMulai) VALUES
(3, 3, '2026-11-10 09:00:00');

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
