import { Role } from '../enums/role';
import { Model } from './model';

export interface IUser extends Model {
  name: string;
  email: string;
  role: Role;
  token: string;
}
