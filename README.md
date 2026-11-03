# Proyek Knet: Aplikasi Manajemen Warnet

## 1. Ide Proyek

**Knet** adalah aplikasi desktop untuk mengelola warnet, dibuat dengan Java Swing (JFrame) di NetBeans dan database MySQL. 

Tujuan:
- Menyimpan data member dan komputer secara terpusat.
- Mencatat sesi pemakaian dan menghitung biaya otomatis.
- Menampilkan riwayat sesi dan total pendapatan per hari.

## 2. Gambaran Aplikasi

### 2.1 Deskripsi Aplikasi

Knet digunakan oleh operator warnet. Operator mengelola data member dan komputer, lalu mencatat sesi pemakaian setiap pelanggan. Saat sesi dimulai, status komputer berubah menjadi Dipakai. Saat sesi diakhiri, sistem menghitung lama pemakaian dan total biaya, lalu status komputer kembali menjadi Tersedia. Seluruh data tersimpan di database MySQL dan ditampilkan melalui form Swing.

### 2.2 Perencanaan Alur

1. Operator membuka aplikasi dan masuk ke menu utama.
2. Operator mendaftarkan member baru di form Member.
3. Operator memastikan data komputer dan tarif per jam sudah terisi di form Komputer.
4. Saat pelanggan datang, operator membuka form Sesi, memilih member dan komputer yang berstatus Tersedia, lalu menekan tombol Mulai.
5. Sistem mencatat jam mulai dan mengubah status komputer menjadi Dipakai.
6. Saat pelanggan selesai, operator memilih sesi yang berjalan lalu menekan tombol Akhiri.
7. Sistem mencatat jam selesai, menghitung total biaya, dan mengubah status komputer menjadi Tersedia.
8. Di akhir hari, operator membuka form Laporan untuk melihat riwayat sesi dan total pendapatan.

### 2.3 Fungsionalitas

| No | Modul | Fungsi |
|---|---|---|
| 1 | Member | Tambah, ubah, hapus, dan cari data member |
| 2 | Komputer | Tambah, ubah, hapus data komputer, atur tarif per jam |
| 3 | Sesi | Mulai sesi, akhiri sesi, hitung total biaya otomatis, ubah status komputer otomatis |
| 4 | Laporan | Tampilkan riwayat sesi, tampilkan total pendapatan per tanggal |
| 5 | Menu Utama | Navigasi ke seluruh form |

### 2.4 Daftar Form

| Form | Isi Utama |
|---|---|
| FormMenuUtama | Tombol navigasi ke form lain |
| FormMember | Field input, tombol Simpan, Ubah, Hapus, Cari, dan JTable |
| FormKomputer | Field input, tombol Simpan, Ubah, Hapus, dan JTable |
| FormSesi | JComboBox member dan komputer, tombol Mulai dan Akhiri, JTable sesi |
| FormLaporan | Pilihan tanggal, JTable riwayat, label total pendapatan |

### 2.5 Pembagian Tugas

| Anggota | Tugas |
|---|---|
| There | Database, class Koneksi, FormMenuUtama, FormMember |
| Meshl | FormKomputer dan pengaturan status komputer |
| Fayha | FormSesi (mulai, akhiri, hitung biaya) dan FormLaporan |

## 3. Rancangan Database

Nama database: `db_knet`

### 3.1 Tabel `member`

| Kolom | Tipe | Keterangan |
|---|---|---|
| idMember | INT, AUTO_INCREMENT | Primary Key |
| nama | VARCHAR(50) | Nama member |
| noHp | VARCHAR(15) | Nomor HP |
| tanggalDaftar | DATE | Tanggal mendaftar |

### 3.2 Tabel `komputer`

| Kolom | Tipe | Keterangan |
|---|---|---|
| idKomputer | INT, AUTO_INCREMENT | Primary Key |
| namaKomputer | VARCHAR(30) | Contoh: PC-01 |
| tarifPerJam | INT | Tarif sewa per jam |
| status | VARCHAR(10) | Tersedia atau Dipakai |

### 3.3 Tabel `sesi`

| Kolom | Tipe | Keterangan |
|---|---|---|
| idSesi | INT, AUTO_INCREMENT | Primary Key |
| idMember | INT | Foreign Key ke member.idMember |
| idKomputer | INT | Foreign Key ke komputer.idKomputer |
| jamMulai | DATETIME | Waktu sesi dimulai |
| jamSelesai | DATETIME | Waktu sesi berakhir, kosong jika masih berjalan |
| totalBiaya | INT | Lama pakai (jam) dikali tarif per jam |

### 3.4 Relasi Antartabel

- Satu member dapat memiliki banyak sesi (1 : N).
- Satu komputer dapat dipakai pada banyak sesi (1 : N).

```
member (1) -------- (N) sesi (N) -------- (1) komputer
```

## 4. Rancangan Class

Urutan penulisan di setiap class mengikuti pola: constructor, getter, setter, input, proses, output.

### 4.1 Class Koneksi

Tugas: membuat dan menyediakan koneksi JDBC ke database `db_knet`.

| Jenis | Nama |
|---|---|
| Atribut | url, user, password, koneksi |
| Method | getKoneksi() |

### 4.2 Class Member

Tugas: menampung data satu member.

| Jenis | Nama |
|---|---|
| Atribut | idMember, nama, noHp, tanggalDaftar |
| Constructor | Member(), Member(idMember, nama, noHp, tanggalDaftar) |
| Getter | getIdMember, getNama, getNoHp, getTanggalDaftar |
| Setter | setIdMember, setNama, setNoHp, setTanggalDaftar |

### 4.3 Class Komputer

Tugas: menampung data satu komputer.

| Jenis | Nama |
|---|---|
| Atribut | idKomputer, namaKomputer, tarifPerJam, status |
| Constructor | Komputer(), Komputer(idKomputer, namaKomputer, tarifPerJam, status) |
| Getter | getIdKomputer, getNamaKomputer, getTarifPerJam, getStatus |
| Setter | setIdKomputer, setNamaKomputer, setTarifPerJam, setStatus |

### 4.4 Class Sesi

Tugas: menampung data satu sesi pemakaian.

| Jenis | Nama |
|---|---|
| Atribut | idSesi, idMember, idKomputer, jamMulai, jamSelesai, totalBiaya |
| Constructor | Sesi(), Sesi(idSesi, idMember, idKomputer, jamMulai, jamSelesai, totalBiaya) |
| Getter | getIdSesi, getIdMember, getIdKomputer, getJamMulai, getJamSelesai, getTotalBiaya |
| Setter | setIdSesi, setIdMember, setIdKomputer, setJamMulai, setJamSelesai, setTotalBiaya |

### 4.5 Class FormMember

| Jenis | Nama |
|---|---|
| Input | inputMember |
| Proses | simpanMember, ubahMember, hapusMember, cariMember |
| Output | tampilMember, kosongkanForm |

### 4.6 Class FormKomputer

| Jenis | Nama |
|---|---|
| Input | inputKomputer |
| Proses | simpanKomputer, ubahKomputer, hapusKomputer, ubahStatusKomputer |
| Output | tampilKomputer, kosongkanForm |

### 4.7 Class FormSesi

| Jenis | Nama |
|---|---|
| Input | inputSesi, isiComboMember, isiComboKomputer |
| Proses | mulaiSesi, akhiriSesi, hitungLamaPakai, hitungTotalBiaya, ubahStatusKomputer |
| Output | tampilSesiBerjalan, tampilTotalBiaya |

### 4.8 Class FormLaporan

| Jenis | Nama |
|---|---|
| Input | inputTanggal |
| Proses | hitungPendapatanHarian |
| Output | tampilRiwayatSesi, tampilTotalPendapatan |

### 4.9 Class FormMenuUtama

| Jenis | Nama |
|---|---|
| Proses | bukaFormMember, bukaFormKomputer, bukaFormSesi, bukaFormLaporan |

### 4.10 Hubungan Antarclass

```
FormMember   --> Member   --> Koneksi --> db_knet
FormKomputer --> Komputer --> Koneksi --> db_knet
FormSesi     --> Sesi, Member, Komputer --> Koneksi --> db_knet
FormLaporan  --> Sesi, Member, Komputer --> Koneksi --> db_knet
FormMenuUtama --> membuka seluruh form
```
