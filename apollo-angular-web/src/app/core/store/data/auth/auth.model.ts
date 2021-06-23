export interface LoginCredentials {
  username: string;
  password: string;
}

export interface LoggedInUser {
  accessToken: string;
  id: number;
  username: string;
  email: string;
  fullName: string;
  roles: string[];
}

