# Knet: Aplikasi Manajemen Warnet

Aplikasi desktop sederhana berbasis Java Swing dan MySQL untuk mengelola member, komputer, dan sesi pemakaian warnet.

## 1. Ide Proyek

Membantu operator warnet yang mencatat sesi pemakaian, perhitungan biaya, dan perbaruan status komputer secara otomatis lewat form dan database.

## 2. Fungsionalitas Inti

| No | Modul | Fungsi |
|---|---|---|
| 1 | Pendaftaran member | Tambah, ubah, hapus, cari |
| 2 | Pendaftaran komputer | Tambah, ubah, hapus, cari, atur tarif per jam |
| 3 | Pendaftaran sesi | Mulai sesi dan akhiri sesi untuk setiap member (biaya terhitung otomatis dan status komputer berubah otomatis) |
| 4 | Laporan | Riwayat sesi member dan total pendapatan per tanggal maupun keseluruhan |

### Alur Singkat

1. Operator mendaftarkan member (FormMember) dan mengisi data komputer (FormKomputer).
2. Operator memilih member dan komputer yang Tersedia, lalu menekan Mulai Sesi (FormSesi).
3. Status komputer otomatis menjadi Dipakai.
4. Operator memilih sesi yang berjalan lalu menekan Akhiri Sesi (FormSesi). Biaya dihitung dan status komputer kembali Tersedia.
5. Operator melihat riwayat dan pendapatan (FormLaporan).

## 3. Rancangan Database

Nama database: `db_knet`

### Tabel

**member** (data di aplikasi: `MemberModel`)
`idMember` INT (PK, auto increment), `nama` VARCHAR(50) NOT NULL, `noHp` VARCHAR(15), `tanggalDaftar` DATE NOT NULL

**komputer** (data di aplikasi: `KomputerModel`)
`idKomputer` INT (PK, auto increment), `namaKomputer` VARCHAR(30) NOT NULL UNIQUE, `tarifPerJam` INT NOT NULL, `status` VARCHAR(10) NOT NULL DEFAULT 'Tersedia' (Tersedia / Dipakai)

**sesi** (data di aplikasi: `SesiModel`)
`idSesi` INT (PK, auto increment), `idMember` INT (FK), `idKomputer` INT (FK), `jamMulai` DATETIME NOT NULL, `jamSelesai` DATETIME (kosong selama sesi berjalan), `totalBiaya` INT NOT NULL DEFAULT 0

### Relasi

```
member (1) -------- (N) sesi (N) -------- (1) komputer
```

### Fitur Database yang Dipakai

| Fitur | Nama | Fungsi |
|---|---|---|
| Foreign Key | sesi.idMember, sesi.idKomputer | Menjaga keterkaitan data antartabel |
| Constraint | NOT NULL, UNIQUE, DEFAULT | Menjaga data tetap valid |
| View | vRiwayatSesi | Gabungan sesi, member, dan komputer (JOIN) untuk riwayat dan laporan |
| Trigger | trgSesiCekKomputer | Sebelum sesi ditambah, menolak jika komputer masih Dipakai |
| Trigger | trgSesiMulai | Setelah sesi ditambah, status komputer menjadi Dipakai |
| Trigger | trgSesiSelesai | Setelah jamSelesai terisi, status komputer menjadi Tersedia |
| Stored Procedure | spAkhiriSesi | Mengisi jamSelesai dan menghitung totalBiaya (dibulatkan ke atas per jam, minimal 1 jam) |
| Query Agregat | SUM + WHERE | Total pendapatan pada tanggal yang dipilih, dan pendapatan keseluruhan |

## 4. Rancangan Class

Proyek dibagi menjadi tiga lapisan. Setiap class model berisi constructor (tanpa parameter dan dengan parameter), getter, dan setter. Input dan output ditangani oleh view, sedangkan proses ke database ditangani oleh repository.

| Package | Class | Tugas |
|---|---|---|
| (utama) | Knet | Titik masuk program, membuka FormMember |
| model | MemberModel, KomputerModel, SesiModel | Menampung data (atribut, constructor, getter, setter) |
| repository | DatabaseUtil, MemberRepository, KomputerRepository, SesiRepository | Koneksi dan akses database lewat JDBC |
| view | FormMember, FormKomputer, FormSesi, FormLaporan | Tampilan form Swing. FormMember juga berfungsi sebagai menu utama. |

Method utama di repository:

| Repository | Method |
|---|---|
| MemberRepository | `getAllMembers`, `searchMembers`, `addMember`, `updateMember`, `deleteMember` |
| KomputerRepository | `getAllKomputer`, `getKomputerTersedia`, `searchKomputer`, `addKomputer`, `updateKomputer`, `updateStatusKomputer`, `deleteKomputer` |
| SesiRepository | `getAllSesi`, `getSesiBerjalan`, `getSesiByTanggal`, `getTotalPendapatan`, `getPendapatanByTanggal`, `addSesi`, `finishSesi` (memanggil `spAkhiriSesi`) |

Alur satu aksi: view menerima input, membungkusnya dalam model, memanggil repository, lalu menampilkan hasilnya ke tabel atau dialog.

## 5. Pembagian File per Anggota

Path file Java berada di `src/main/java/com/mycompany/knet/`.

### Theresia : Member dan Fondasi Database

| File | Keterangan |
|---|---|
| resources/script.sql | Bagian pembuatan database, tabel, constraint, dan data awal |
| repository/DatabaseUtil.java | Koneksi JDBC |
| model/MemberModel.java | Data member |
| repository/MemberRepository.java | CRUD member |
| view/FormMember.java | Form member dan menu navigasi |
| README.md | Dokumentasi proyek |

### Meshal : Komputer

| File | Keterangan |
|---|---|
| model/KomputerModel.java | Data komputer |
| repository/KomputerRepository.java | CRUD komputer |
| view/FormKomputer.java | Form komputer |

### Fayha : Sesi, Laporan, dan Logika Database

| File | Keterangan |
|---|---|
| resources/script.sql | Bagian view, trigger, dan stored procedure |
| model/SesiModel.java | Data sesi dan riwayat |
| repository/SesiRepository.java | Mulai sesi, akhiri sesi, riwayat, pendapatan |
| view/FormSesi.java | Form sesi |
| view/FormLaporan.java | Form laporan |

File `script.sql` dikerjakan bersama: bagian tabel dan data awal member/komputer ditulis lebih dulu, lalu view, trigger, dan stored procedure di bawahnya, dan data awal sesi ditaruh paling akhir supaya trigger sudah aktif.

## 6. Cara Menjalankan

Kebutuhan: JDK 21, Maven, dan MySQL.

1. Jalankan `src/main/resources/script.sql` di MySQL (klien `mysql`, Workbench, atau HeidiSQL) untuk membuat database `db_knet`, termasuk view, trigger, stored procedure, dan data awal.
2. Sesuaikan url (host dan port), user, dan password di `src/main/java/com/mycompany/knet/repository/DatabaseUtil.java`.
3. Buka proyek di NetBeans (Maven akan mengunduh MySQL Connector/J otomatis), lalu jalankan class `Knet`. Form yang pertama tampil adalah FormMember.
