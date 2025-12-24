export interface Student {
  id: number;
  studentId: string;
  name: string;
  email: string;
  major?: string;
  phoneNumber?: string;
  year?: string;
  gpa?: number;
  credits?: number;
  totalCredits?: number;
  attendance?: number;
  role: string;
}

export interface UpdateStudentRequest {
  name?: string;
  email?: string;
  password?: string;
  major?: string;
  phoneNumber?: string;
}