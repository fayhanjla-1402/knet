# Knet: Aplikasi Manajemen Warnet

Aplikasi desktop sederhana berbasis Java Swing dan MySQL untuk mengelola member, komputer, dan sesi pemakaian warnet. Fokus proyek ada di sisi database: aplikasi dibuat ringkas, tetapi fitur database dimanfaatkan maksimal.

## 1. Ide Proyek

Operator warnet sering mencatat pemakaian komputer secara manual sehingga rawan salah hitung dan status komputer tidak jelas. Knet mencatat sesi pemakaian, menghitung biaya, dan memperbarui status komputer secara otomatis lewat database.

## 2. Fungsionalitas Inti

| No | Modul | Fungsi |
|---|---|---|
| 1 | Member | Tambah, ubah, hapus, cari |
| 2 | Komputer | Tambah, ubah, hapus, atur tarif per jam |
| 3 | Sesi | Mulai sesi dan akhiri sesi (biaya dan status komputer otomatis) |
| 4 | Laporan | Riwayat sesi dan total pendapatan per tanggal |

### Alur Singkat

1. Operator mendaftarkan member dan mengisi data komputer.
2. Operator memilih member dan komputer yang Tersedia, lalu menekan Mulai.
3. Status komputer otomatis menjadi Dipakai.
4. Operator menekan Akhiri, lalu biaya dihitung dan status komputer kembali Tersedia.
5. Operator melihat riwayat dan pendapatan di form Laporan.

## 3. Rancangan Database

Nama database: `db_knet`

### Tabel

**member**: idMember (PK), nama, noHp, tanggalDaftar

**komputer**: idKomputer (PK), namaKomputer (UNIQUE), tarifPerJam, status (Tersedia / Dipakai)

**sesi**: idSesi (PK), idMember (FK), idKomputer (FK), jamMulai, jamSelesai, totalBiaya

### Relasi

```
member (1) -------- (N) sesi (N) -------- (1) komputer
```

### Fitur Database yang Dipakai

| Fitur | Nama | Fungsi |
|---|---|---|
| Foreign Key | sesi.idMember, sesi.idKomputer | Menjaga keterkaitan data antartabel |
| Constraint | NOT NULL, UNIQUE, DEFAULT | Menjaga data tetap valid |
| View | vRiwayatSesi | Gabungan sesi, member, dan komputer (JOIN) untuk laporan |
| Trigger | trgSesiMulai | Setelah sesi ditambah, status komputer menjadi Dipakai |
| Trigger | trgSesiSelesai | Setelah jamSelesai terisi, status komputer menjadi Tersedia |
| Stored Procedure | spAkhiriSesi | Mengisi jamSelesai dan menghitung totalBiaya |
| Query Agregat | GROUP BY + SUM | Total pendapatan per tanggal |

## 4. Rancangan Class

Pola penulisan setiap class: constructor, getter, setter, input, proses, output.

| Package | Class | Tugas |
|---|---|---|
| entity | Member, Komputer, Sesi | Menampung data (atribut, constructor, getter, setter) |
| model | MemberTableModel, KomputerTableModel, SesiTableModel | Mengatur data untuk tampilan JTable |
| repository | Koneksi, MemberRepository, KomputerRepository, SesiRepository | Akses database lewat JDBC |
| view | FormMenuUtama, FormMember, FormKomputer, FormSesi, FormLaporan | Tampilan form Swing |

Method utama di repository: `simpanData`, `ubahData`, `hapusData`, `cariData`, `ambilSemuaData`. Khusus SesiRepository: `mulaiSesi`, `akhiriSesi`, `ambilRiwayat`, `ambilPendapatanHarian`.

## 5. Pembagian File per Anggota

### Theresia : Member dan Fondasi Database

| File | Keterangan |
|---|---|
| resources/script.sql | Bagian pembuatan database, tabel, constraint, dan data awal |
| repository/Koneksi.java | Koneksi JDBC |
| entity/Member.java | Data member |
| model/MemberTableModel.java | Tabel member |
| repository/MemberRepository.java | CRUD member |
| view/FormMember.java | Form member |
| README.md | Dokumentasi proyek |

### Meshal : Komputer dan Menu

| File | Keterangan |
|---|---|
| entity/Komputer.java | Data komputer |
| model/KomputerTableModel.java | Tabel komputer |
| repository/KomputerRepository.java | CRUD komputer |
| view/FormKomputer.java | Form komputer |
| view/FormMenuUtama.java | Menu navigasi |

### Fayha : Sesi, Laporan, dan Logika Database

| File | Keterangan |
|---|---|
| resources/script.sql | Bagian view, trigger, dan stored procedure |
| entity/Sesi.java | Data sesi |
| model/SesiTableModel.java | Tabel sesi dan riwayat |
| repository/SesiRepository.java | Mulai sesi, akhiri sesi, riwayat, pendapatan |
| view/FormSesi.java | Form sesi |
| view/FormLaporan.java | Form laporan |

File `script.sql` dikerjakan bersama: Anggota 1 menulis bagian tabel, Anggota 3 menambahkan view, trigger, dan stored procedure di bagian bawahnya.

## 6. Cara Menjalankan

1. Jalankan `src/main/resources/script.sql` di MySQL untuk membuat database `db_knet`.
2. Sesuaikan url, user, dan password di `Koneksi.java`.
3. Buka proyek di NetBeans, lalu jalankan `FormMenuUtama`.