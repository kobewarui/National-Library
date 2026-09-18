# National Library

A library management system built with Spring Boot and Thymeleaf. Librarians
manage the catalogue and lending; members sign in to see what they have on loan
and what they owe.

## Features

**Librarian (admin)**

- Add, edit and delete **authors**
- Add, edit and delete **books**, each linked to an author
- Register **members** — students and professionals — with an optional profile photo
- Lend a book to a member, and record its return
- **Report** page: stock at a glance, plus every book currently on loan and how
  overdue it is

**Member (student / professional)**

- Own dashboard listing the books they currently have on loan
- Outstanding fines total

Members only ever see their own dashboard — the catalogue and lending screens
are restricted to admins.

## Tech stack

| | |
|---|---|
| Java | 17 |
| Framework | Spring Boot 3.0.6 |
| Views | Thymeleaf + [SB Admin 2](https://startbootstrap.com/theme/sb-admin-2) |
| Security | Spring Security (form login, role-based access) |
| Persistence | Spring Data JPA / Hibernate |
| Database | MySQL |
| Build | Maven (wrapper included) |

## Getting started

### Prerequisites

- JDK 17
- MySQL running on `localhost:3306`

Maven is not required — the project ships with the Maven Wrapper, which
downloads Maven on first use.

The wrapper needs `JAVA_HOME` set, which is separate from having `java` on your
PATH. If you see `Error: JAVA_HOME not found in your environment`:

```bash
# Windows (PowerShell, current session)
$env:JAVA_HOME = "C:\Program Files\Java\jdk-17.0.2"

# macOS / Linux
export JAVA_HOME=$(dirname $(dirname $(readlink -f $(which java))))
```

Set it permanently in your system environment variables to avoid repeating it.

### 1. Create the database

```sql
CREATE DATABASE national_library;
```

Tables are created automatically on first run (`spring.jpa.hibernate.ddl-auto=update`).

### 2. Set your database credentials

`src/main/resources/application.properties` defaults to the MySQL `root` account
with a blank password. Change these to match your setup:

```properties
spring.datasource.username=root
spring.datasource.password=
```

### 3. Run it

```bash
./mvnw spring-boot:run        # macOS / Linux
mvnw.cmd spring-boot:run      # Windows
```

Then open **http://localhost:8080/user/login**.

## First run

The database starts empty — there are no seeded accounts.

1. Go to **Create an Account!** on the login page. This registers an **admin**.
2. Sign in. You land on the admin dashboard.
3. Add an **author** first, since every book needs one.
4. Add a **book**.
5. Use **ADD USER** to register members. Pick *Student* or *Professional* —
   these are the accounts that can borrow. Members can sign in with the email
   and password you set here.
6. Use **Borrows → Add Borrow** to lend a book out.

## How lending works

A book can only be on loan to one person at a time. Once lent, it disappears
from the Add Borrow dropdown and shows as *Borrowed* in the catalogue until it
is returned.

When lending, the librarian sets the borrow date, the due date (defaults to two
weeks out) and the fine rate for that loan.

## How fines work

Nothing is owed on or before the due date. After that:

```
fine = days late × the fine-per-day set on that loan
```

The rate defaults to **KSh 10 per day** and can be changed per loan on the Add
Borrow form. The fine is calculated at the moment the book is returned and
stored on that loan record, so it never changes afterwards.

A member's total is the sum of the fines across their loans, shown on their
dashboard. The Report page shows what a book still out *would* cost if it came
back today.

To change the default rate, edit `DEFAULT_FINE_PER_DAY` in
`services/BorrowService.java`.

## Project structure

```
src/main/java/com/kelvin/nationallibraryproject/
├── config/          Spring Security, JWT filter, static resource mapping
├── controllers/     Authors, Books, Borrows, Profile/members, Home
├── models/          User (Admin/Student/Professional), Author, Book, Borrow
├── repositories/    Spring Data JPA repositories
├── services/        Business logic, including fine calculation
└── utils/           File upload helper

src/main/resources/
├── templates/       Thymeleaf pages; layouts.html (admin) and
│                    layoutstwo.html (member) hold the shared fragments
└── static/          CSS, JS and the SB Admin 2 vendor assets
```

Uploaded profile photos are written to a `user-photos/` directory at the project
root and served from `/user-photos/**`. It is not tracked by git.

## Notes

- `JwtService` and `JwtAthFilter` exist and are wired, but nothing uses them yet.
  They are groundwork for a REST API; the web app itself uses session-based form
  login.
- CSRF protection is disabled in `SecurityConfig`.
- Uploads are capped at 5 MB per file.
