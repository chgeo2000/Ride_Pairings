INSERT INTO ride_pairings.employee(external_id, first_name, last_name, birth_date, phone_number, email,
                                   new_employee, hourly_wage, off_day)
VALUES

    (uuid_generate_v4(), 'Razvan', 'Macovei', '2000-11-13', '0770910617', 'razvanmacovei@yahoo.com', true, 14, 'Monday'),

    (uuid_generate_v4(), 'Cristoce', 'Onofrei', '2001-08-12', '0770910617', 'cristoceonofrei@yahoo.com', true, 14, 'Tuesday'),

    (uuid_generate_v4(), 'David', 'Navarro', '1999-05-10', '0770910617', 'davidnavarro@yahoo.com', true, 14, 'Thursday'),

    (uuid_generate_v4(), 'Melisa', 'Krista', '2000-11-09', '0770910617', 'melisakrista@yahoo.com', true, 14, 'Friday'),

    (uuid_generate_v4(), 'Ana', 'Blanco', '1977-10-08', '0770910617', 'anablanco@yahoo.com', true, 14, 'Saturday'),

    (uuid_generate_v4(), 'Jorge', 'Nelam', '1982-03-05', '0770910617', 'jorgenelam@yahoo.com', true, 14, 'Sunday'),

    (uuid_generate_v4(), 'Ju', 'Wenjun', '1991-04-24', '0770910617', 'juwenjun@yahoo.com', true, 14, 'Monday');
