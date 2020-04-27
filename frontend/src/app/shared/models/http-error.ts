export interface IApiError {
  status: string;
  timestamp: Date;
  message: string;
  errors: Array<string>;
}
