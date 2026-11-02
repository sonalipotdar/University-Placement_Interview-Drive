
CREATE TABLE IF NOT EXISTS students (
    roll_no VARCHAR(30) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    cgpa DECIMAL(4,2) NOT NULL,
    active_backlogs INT DEFAULT 0,
    placement_status VARCHAR(30) DEFAULT 'UNPLACED'
);

CREATE TABLE IF NOT EXISTS companies (
    company_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    min_cgpa DECIMAL(4,2) NOT NULL,
    max_backlogs INT NOT NULL,
    package_lpa DECIMAL(5,2) NOT NULL
);

CREATE TABLE IF NOT EXISTS applications (
    app_id INT AUTO_INCREMENT PRIMARY KEY,
    roll_no VARCHAR(30) NOT NULL,
    company_id INT NOT NULL,
    interview_status VARCHAR(30) NOT NULL,
    FOREIGN KEY (roll_no) REFERENCES students(roll_no),
    FOREIGN KEY (company_id) REFERENCES companies(company_id),
    UNIQUE (roll_no, company_id)
);
