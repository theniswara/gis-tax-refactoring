export interface Admin {
  id: number;
  username: string;
  email: string;
  fullName: string;
  role: string;
  isActive: boolean;
  createdAt?: Date;
  updatedAt?: Date;
  lastLogin?: Date;
}

export interface AdminLoginRequest {
  username: string;
  password: string;
}

export interface CreateAdminRequest {
  username: string;
  email: string;
  password: string;
  fullName: string;
}

export interface AdminLoginResponse {
  success: boolean;
  message: string;
  accessToken: string;
  refreshToken: string;
  user: Admin;
}

export interface DashboardStats {
  totalStudents: number;
  totalAdmins: number;
}