export interface Course {
  code: string;
  name: string;
  instructor: string;
  grade: string;
  progress: number;
  credits: number;
  schedule: string;
  room: string;
}

export interface Assignment {
  course: string;
  title: string;
  dueDate: string;
  status: string;
}

export interface Announcement {
  title: string;
  date: string;
  important: boolean;
}

export interface Student {
  id: string;
  name: string;
  email: string;
  major: string;
  year: string;
  gpa: number;
  credits: number;
  totalCredits: number;
  attendance: number;
  currentCourses?: Course[];  // ✅ FIXED: Made optional
  upcomingAssignments?: Assignment[];  // ✅ FIXED: Made optional
  announcements?: Announcement[];  // ✅ FIXED: Made optional
}