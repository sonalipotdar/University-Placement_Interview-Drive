
INSERT IGNORE INTO students
(roll_no, name, cgpa, active_backlogs, placement_status)
VALUES
('NIT001', 'Rahul Sharma', 8.20, 0, 'UNPLACED'),
('NIT002', 'Priya Singh', 9.10, 0, 'UNPLACED'),
('NIT003', 'Amit Das', 6.50, 2, 'UNPLACED'),
('NIT004', 'Sneha Roy', 7.80, 1, 'UNPLACED'),
('NIT005', 'Arjun Kumar', 8.50, 0, 'UNPLACED');

INSERT IGNORE INTO companies
(name, min_cgpa, max_backlogs, package_lpa)
VALUES
('TCS', 7.00, 0, 6.00),
('Infosys', 6.50, 2, 4.50),
('Capgemini', 7.50, 0, 5.50),
('Accenture', 8.00, 0, 7.00),
('Wipro', 6.00, 2, 4.00);
