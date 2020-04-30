import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class EnrollmentService {
  private END_POINT_BASE = `${environment.API_URL}/enrollments`;

  private END_POINT_CHECK_IN(enrollmentId: number): string {
    return `${this.END_POINT_BASE}/${enrollmentId}/check-in`;
  }

  constructor(private http: HttpClient) {}

  checkIn(enrollmentId: number): Observable<void> {
    return this.http.patch<void>(this.END_POINT_CHECK_IN(enrollmentId), {});
  }
}
