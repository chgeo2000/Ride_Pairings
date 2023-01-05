INSERT INTO ride_pairings.ride(external_id, name, description, location, ride_type, minimum_height, maximum_height, maximum_number_of_ride_attendants, is_closed)
VALUES
    (uuid_generate_v4(), 'Chuck Wagon', 'Built just for the little pioneers, a miniature western themed version of the full sized sky wheel!',
     'Outlaw Gulch', 'Kiddie', 42, null, 1, false),

    (uuid_generate_v4(), 'Junior Jockeys', 'A fun horse ride through Bernie"s Barnyard',
     'South side of Bernies"s Barnyard', 'Kiddie', 43, null, 2, false),

    (uuid_generate_v4(), 'Shakin" Bacon', 'An exciting ride that shakes guests up and down and side to side',
     'West side of Bernies"s Barnyard', 'Kiddie', 36, null, 1, false),

    (uuid_generate_v4(), 'Frog Hopper', 'Let your children take a ride on our mini-space shot built especially for them',
     'Just west side of the Space Shot', 'Kiddie', 36, null, 1, false),

    (uuid_generate_v4(), 'Phoenix', 'Take the family on a fun filled ride unlike any other at Adventureland. Our newest coaster reaches speeds up to 40mph.',
     'The Boulevard', 'Family', 48, null, 4, false),

    (uuid_generate_v4(), 'G-Force', 'Celebrate the international Space Station by taking a ride on the G-Force.',
     'In the Main Street Arcade', 'Family', 52, null, 2, false),

    (uuid_generate_v4(), 'Scrambler', 'This twisting, turning and spinning ride is sure to challenge even the bravest thrill seekers',
     'The Boulevard', 'Family', 48, null, 2, false),

    (uuid_generate_v4(), 'Underground', 'Tour our old mine and try to solve the mystery of Bad Bob.',
     'Next to the Frantic Freeway', 'Family', 42, null, 3, false),

    (uuid_generate_v4(), 'Monster', 'Adventureland"s first ever Infinity Roller Coaster',
     'Center of Adventureland', 'Thrill', 48, null, 4, false),

    (uuid_generate_v4(), 'Tornado', 'Adventureland"s first ever wooden roller coaster.',
     'County Fair Games', 'Thrill', 48, null, 4, false),

    (uuid_generate_v4(), 'Outlaw', 'A fast paced, fun filled wooden coaster. With a maximum g-force of 3.2 - it"ll steal your breath away.',
     'Outlaw Gulch', 'Thrill', 48, null, 4, false),

    (uuid_generate_v4(), 'Sidewinder', 'A ride that combines the best of spinning and swinging, the Sidewinder is a great ride.',
     'Near the entrance to Outlaw Gulch', 'Thrill', 48, null, 4, true),

    (uuid_generate_v4(), 'Storm Chaser', 'A giant swing that takes you to 260 feet off the ground and spins those brave enough to ride it around at 35 mph',
     'Next to the Monster', 'Thrill', 52, null, 4, true);









