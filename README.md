# Knet: Aplikasi Manajemen Warnet

Aplikasi desktop sederhana berbasis Java Swing dan MySQL untuk mengelola member, komputer, dan sesi pemakaian warnet.

## 1. Ide Proyek

Membantu operator warnet yang mencatat sesi pemakaian, perhitungan biaya, dan perbaruan status komputer secara otomatis lewat form dan database.

## 2. Fungsionalitas Inti

| No | Modul | Fungsi |
|---|---|---|
| 1 | Pendaftaran member | Tambah, ubah, hapus, cari |
| 2 | Pendaftaran komputer | Tambah, ubah, hapus, cari |
| 3 | Pendaftaran sesi | Mulai sesi dan akhiri sesi untuk setiap member (biaya terhitung otomastis dan status komputer berubah otomatis) |
| 4 | Laporan | Riwayat sesi member dan total pendapatan per tanggal |

### Alur Singkat

1. Operator mendaftarkan member (Form member) dan mengisi data komputer (Form komputer).
2. Operator memilih member dan komputer yang Tersedia, lalu menekan Mulai (Form sesi).
3. Status komputer otomatis menjadi Dipakai (Form komputer).
4. Operator menekan Akhiri (Form sesi), lalu biaya dihitung dan status komputer kembali Tersedia.
5. Operator melihat riwayat dan pendapatan (Form Laporan).

## 3. Rancangan Database

Nama database: `db_knet`

### Tabel

**Member (MemberModel.java)**: 
int idMember (PK), String nama, String noHp, Date tanggalDaftar

**Komputer (KomputerModel.java)**: 
int idKomputer (PK), String namaKomputer (UNIQUE), int tarifPerJam, String status (Tersedia / Dipakai)

**sesi (SesiModel.java)**: 
int idSesi (PK), int idMember (FK), int idKomputer (FK), Timestamp jamMulai, TimeStamp jamSelesai, int totalBiaya

### Relasi

```
Member (1) -------- (N) Sesi (N) -------- (1) Komputer
```

### Fitur Database yang Dipakai

| Fitur | Nama | Fungsi |
|---|---|---|
| Foreign Key | sesi.idMember, sesi.idKomputer | Menjaga keterkaitan data antartabel |
| Constraint | NOT NULL, UNIQUE, DEFAULT | Menjaga data tetap valid |
| View | vRiwayatSesi | Gabungan sesi, member, dan komputer (JOIN) untuk laporan |
| Trigger | trgSesiCekKomputer | Sebelum sesi ditambah, menolak jika komputer masih Dipakai |
| Trigger | trgSesiMulai | Setelah sesi ditambah, status komputer menjadi Dipakai |
| Trigger | trgSesiSelesai | Setelah jamSelesai terisi, status komputer menjadi Tersedia |
| Stored Procedure | spAkhiriSesi | Mengisi jamSelesai dan menghitung totalBiaya (dibulatkan ke atas per jam, minimal 1 jam) |
| Query Agregat | SUM + WHERE | Total pendapatan pada tanggal yang dipilih, dan pendapatan keseluruhan |

## 4. Rancangan Class

Pola penulisan setiap class: constructor (tanpa parameter dan dengan parameter), getter, setter, input, proses, output.

| Package | Class | Tugas |
|---|---|---|
| entity | Member, Komputer, Sesi | Menampung data (atribut, constructor, getter, setter) |
| model | MemberModel, KomputerModel, SesiModel | Mengatur data untuk tampilan JTable |
| repository | Koneksi, MemberRepository, KomputerRepository, SesiRepository | Akses database lewat JDBC |
| view | FormMenuUtama, FormMember, FormKomputer, FormSesi, FormLaporan | Tampilan form Swing |

Method utama di repository: `simpanData`, `ubahData`, `hapusData`, `cariData`, `ambilSemuaData`. Khusus SesiRepository: `mulaiSesi`, `akhiriSesi` (memanggil `spAkhiriSesi`), `ambilSesiBerjalan`, `ambilRiwayat`, `ambilRiwayatPerTanggal`, `ambilPendapatanPerTanggal`, `ambilPendapatanTotal`.

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

File `script.sql` dikerjakan bersama: bagian tabel dan data awal member/komputer ditulis lebih dulu, lalu view, trigger, dan stored procedure di bawahnya, dan data awal sesi ditaruh paling akhir supaya trigger sudah aktif.

## 6. Cara Menjalankan

1. Jalankan `src/main/resources/script.sql` di MySQL (klien `mysql`, Workbench, atau HeidiSQL) untuk membuat database `db_knet`, termasuk view, trigger, dan stored procedure.
2. Sesuaikan url, user, dan password di `Koneksi.java`.
3. Buka proyek di NetBeans (Maven akan mengunduh MySQL Connector/J otomatis), lalu jalankan class `Knet` atau `FormMenuUtama`.