INSERT INTO ride_pairings.employee(external_id, first_name, last_name, birth_date, phone_number, email,
                                   new_employee, hourly_wage, off_day)
VALUES

    (uuid_generate_v4(), 'Federica', 'Amato', '1995-10-22', '4431234444', 'federicaamato@yahoo.com', false, 30, 'Sunday'),

    (uuid_generate_v4(), 'April', 'Sauls', '1986-06-17', '4431234444', 'aprilsauls@yahoo.com', false, 50, 'Sunday'),

    (uuid_generate_v4(), 'Ben', 'Ulim', '2001-09-24', '4431234444', 'benulim@yahoo.com', false, 44, 'Sunday'),

    (uuid_generate_v4(), 'Nicolas', 'Cremer', '2001-10-27', '4431234444', 'nicolascremer@yahoo.com', false, 30, 'Sunday'),

    (uuid_generate_v4(), 'Erim', 'Hilker', '2001-07-29', '4431234444', 'erimhilker@yahoo.com', false, 30, 'Sunday'),

    (uuid_generate_v4(), 'Jeffrey', 'Seller', '2001-02-21', '4431234444', 'jeffreyseller@yahoo.com', false, 30, 'Sunday'),

    (uuid_generate_v4(), 'Newman', 'Rex', '2001-06-12', '4431234444', 'newmanrex@yahoo.com', false, 30, 'Sunday');