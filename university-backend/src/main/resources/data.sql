-- Insert Programs
INSERT INTO programs (name, degree, duration, credits, tuition, description, image, field, rating, students, employment_rate) VALUES
('Computer Science', 'Bachelor of Science', '4 years', 120, '$15,000/year', 'Comprehensive program covering programming, algorithms, AI, machine learning, and software engineering.', 'https://images.unsplash.com/photo-1517694712202-14dd9538aa97?w=400', 'Technology', 4.8, 850, 96),
('Business Administration', 'Bachelor of Business Administration', '4 years', 120, '$12,000/year', 'Learn business strategy, finance, marketing, entrepreneurship, and international business management.', 'https://images.unsplash.com/photo-1454165804606-c3d57bc86b40?w=400', 'Business', 4.7, 1200, 94),
('Medicine', 'Doctor of Medicine (MD)', '6 years', 240, '$25,000/year', 'Comprehensive medical training with extensive clinical experience and specialized rotations.', 'https://images.unsplash.com/photo-1576091160399-112ba8d25d1d?w=400', 'Health Sciences', 4.9, 450, 99),
('Mechanical Engineering', 'Bachelor of Engineering', '4 years', 128, '$16,000/year', 'Study mechanics, thermodynamics, robotics, and advanced manufacturing technologies.', 'https://images.unsplash.com/photo-1581094794329-c8112a89af12?w=400', 'Engineering', 4.6, 680, 95),
('Graphic Design', 'Bachelor of Fine Arts', '3 years', 90, '$11,000/year', 'Creative arts program focusing on digital design, branding, UX/UI, and visual communication.', 'https://images.unsplash.com/photo-1561070791-2526d30994b5?w=400', 'Arts', 4.5, 420, 89),
('Psychology', 'Bachelor of Science', '4 years', 120, '$13,000/year', 'Study human behavior, cognitive processes, mental health, and therapeutic techniques.', 'https://images.unsplash.com/photo-1527689368864-3a821dbccc34?w=400', 'Social Sciences', 4.7, 750, 91);

-- Insert Program Highlights
INSERT INTO program_highlights (program_id, highlight) VALUES
(1, 'AI & Machine Learning'), (1, 'Software Development'), (1, 'Cybersecurity'), (1, 'Data Science'),
(2, 'Strategic Management'), (2, 'Digital Marketing'), (2, 'Finance'), (2, 'Entrepreneurship'),
(3, 'Clinical Training'), (3, 'Surgery'), (3, 'Internal Medicine'), (3, 'Research'),
(4, 'Robotics'), (4, 'CAD Design'), (4, 'Manufacturing'), (4, 'Renewable Energy'),
(5, 'Digital Design'), (5, 'UX/UI'), (5, 'Branding'), (5, 'Motion Graphics'),
(6, 'Clinical Psychology'), (6, 'Cognitive Science'), (6, 'Research Methods'), (6, 'Counseling');

-- Insert Program Careers
INSERT INTO program_careers (program_id, career) VALUES
(1, 'Software Engineer'), (1, 'Data Scientist'), (1, 'AI Specialist'), (1, 'Systems Architect'),
(2, 'Business Analyst'), (2, 'Marketing Manager'), (2, 'Entrepreneur'), (2, 'Financial Consultant'),
(3, 'Physician'), (3, 'Surgeon'), (3, 'Medical Researcher'), (3, 'Specialist'),
(4, 'Mechanical Engineer'), (4, 'Design Engineer'), (4, 'Project Manager'), (4, 'Consultant'),
(5, 'Graphic Designer'), (5, 'UX Designer'), (5, 'Creative Director'), (5, 'Brand Strategist'),
(6, 'Psychologist'), (6, 'Counselor'), (6, 'Researcher'), (6, 'HR Specialist');

-- Insert News
INSERT INTO news (title, date, category, author, excerpt, image, content, views, likes) VALUES
('Excellence University Ranks Among Top 100 Universities Globally', '2025-10-18', 'Achievement', 'Dr. Sarah Mitchell', 'Our university has been recognized in the latest QS World University Rankings, climbing 15 positions to secure a spot in the top 100...', 'https://images.unsplash.com/photo-1523050854058-8df90110c9f1?w=600', 'Full article content here...', 5420, 892),
('New AI Research Center Opens with $50M Funding', '2025-10-15', 'Research', 'Prof. James Chen', 'State-of-the-art artificial intelligence research facility launches this month, featuring advanced GPU clusters and collaborative spaces...', 'https://images.unsplash.com/photo-1532094349884-543bc11b234d?w=600', 'Full article content here...', 4230, 756),
('Engineering Student Wins National Innovation Competition', '2025-10-12', 'Student Achievement', 'Media Relations', 'Fourth-year mechanical engineering student develops revolutionary sustainable energy solution...', 'https://images.unsplash.com/photo-1524178232363-1fb2b075b655?w=600', 'Full article content here...', 3890, 1024),
('Partnership with Leading Tech Companies Announced', '2025-10-08', 'Partnership', 'Corporate Relations', 'New collaboration will provide internship opportunities and cutting-edge research funding...', 'https://images.unsplash.com/photo-1552664730-d307ca884978?w=600', 'Full article content here...', 4567, 823),
('Medical School Breakthrough in Cancer Research', '2025-10-05', 'Research', 'Dr. Emily Rodriguez', 'Groundbreaking discovery in immunotherapy could revolutionize cancer treatment protocols...', 'https://images.unsplash.com/photo-1579154204601-01588f351e67?w=600', 'Full article content here...', 6234, 1456),
('Campus Sustainability Initiative Achieves Carbon Neutrality', '2025-10-01', 'Sustainability', 'Environmental Office', 'University becomes first in the region to achieve complete carbon neutrality ahead of schedule...', 'https://images.unsplash.com/photo-1497435334941-8c899ee9e8e9?w=600', 'Full article content here...', 3456, 678);

-- Insert Events
INSERT INTO events (title, date, time, location, type, description, image, registrations, capacity) VALUES
('Fall Open House 2025', '2025-11-15', '9:00 AM - 4:00 PM', 'Main Campus, Student Center', 'Admissions', 'Visit campus, meet faculty, attend sample lectures, and explore our facilities.', 'https://images.unsplash.com/photo-1540575467063-178a50c2df87?w=400', 450, 500),
('Career Fair 2025', '2025-11-20', '10:00 AM - 6:00 PM', 'University Convention Center', 'Career', 'Meet with 150+ employers from top companies across various industries.', 'https://images.unsplash.com/photo-1511578314322-379afb476865?w=400', 1200, 2000),
('Annual Research Symposium', '2025-12-01', '1:00 PM - 7:00 PM', 'Science Building, Conference Hall A', 'Academic', 'Showcase of groundbreaking research from faculty and graduate students.', 'https://images.unsplash.com/photo-1591115765373-5207764f72e7?w=400', 320, 400),
('International Students Welcome Week', '2025-11-25', '9:00 AM - 5:00 PM', 'International Center', 'Student Life', 'Orientation and welcome activities for international students.', 'https://images.unsplash.com/photo-1523050854058-8df90110c9f1?w=400', 280, 350),
('Innovation & Entrepreneurship Summit', '2025-12-10', '8:00 AM - 6:00 PM', 'Business School Auditorium', 'Business', 'Learn from successful entrepreneurs and pitch your startup ideas.', 'https://images.unsplash.com/photo-1559136555-9303baea8ebd?w=400', 380, 500),
('Arts & Culture Festival', '2025-12-15', '12:00 PM - 10:00 PM', 'Campus Grounds', 'Cultural', 'Celebrate diversity with performances, exhibitions, and international cuisine.', 'https://images.unsplash.com/photo-1514306191717-452ec28c7814?w=400', 890, 1500);

-- Insert Event Speakers
INSERT INTO event_speakers (event_id, speaker) VALUES
(1, 'President Dr. Williams'), (1, 'Dean of Admissions'),
(2, 'Industry Leaders'), (2, 'Alumni Panel'),
(3, 'Dr. Chen'), (3, 'Dr. Patel'), (3, 'Dr. Johnson'),
(4, 'International Office Staff'),
(5, 'Serial Entrepreneurs'), (5, 'VC Investors'), (5, 'Alumni Founders'),
(6, 'Student Performers'), (6, 'Cultural Groups');

-- Insert Sample Student
INSERT INTO students (student_id, name, email, password, major, year, gpa, credits, total_credits, attendance) VALUES
('STU2025001234', 'John Doe', 'john.doe@excellence.edu', 'password123', 'Computer Science', 'Junior', 3.85, 90, 120, 95);

-- Insert Sample Courses for Student
INSERT INTO courses (code, name, instructor, grade, progress, credits, schedule, room, student_id) VALUES
('CS301', 'Data Structures & Algorithms', 'Dr. Sarah Smith', 'A-', 75, 4, 'Mon, Wed 10:00-11:30 AM', 'CS Building 301', 1),
('MATH205', 'Linear Algebra', 'Prof. Michael Johnson', 'B+', 70, 3, 'Tue, Thu 2:00-3:15 PM', 'Math Building 205', 1),
('ENG210', 'Technical Writing', 'Dr. Emily Williams', 'A', 80, 3, 'Mon, Wed 1:00-2:15 PM', 'Liberal Arts 102', 1),
('CS350', 'Database Systems', 'Prof. David Brown', 'A-', 65, 4, 'Tue, Thu 10:00-11:30 AM', 'CS Building 405', 1),
('BUS101', 'Business Ethics', 'Dr. Lisa Davis', 'B', 60, 2, 'Fri 9:00-11:00 AM', 'Business Building 201', 1);

-- Insert Sample Assignments
INSERT INTO assignments (course, title, due_date, status, student_id) VALUES
('CS301', 'Algorithm Analysis Project', '2025-10-25', 'pending', 1),
('MATH205', 'Problem Set 8', '2025-10-23', 'pending', 1),
('CS350', 'Database Design Assignment', '2025-10-28', 'pending', 1);

-- Insert Sample Announcements
INSERT INTO announcements (title, date, important, student_id) VALUES
('Midterm Schedule Released', '2025-10-20', true, 1),
('Career Fair Registration Open', '2025-10-19', false, 1),
('Library Extended Hours', '2025-10-18', false, 1);
