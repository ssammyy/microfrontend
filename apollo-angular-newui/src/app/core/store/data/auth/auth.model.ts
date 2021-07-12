export interface LoginCredentials {
  username: string;
  password: string;
}

export class LoggedInUser {
  accessToken = '';
  id = 0;
  username = '';
  email = '';
  fullName = '';
  roles: string[] = [];
  expiry = Date();
}

export class SendTokenRequestDto {
  username = '';
}

export class ValidateTokenRequestDto {
  username = '';
  token = '';
}



