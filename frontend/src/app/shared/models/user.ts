import { Role } from '../enums/role';

export interface IUser {
  id: number;
  name: string;
  email: string;
  role: Role;
  token: string;
}
