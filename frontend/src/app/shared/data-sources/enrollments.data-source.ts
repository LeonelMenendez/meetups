import { of } from 'rxjs';
import { catchError, finalize } from 'rxjs/operators';
import { UserService } from 'src/app/core/services/user.service';
import { IEnrollmentResponse } from 'src/app/shared/models/enrollment';

import { BaseDataSource } from './base.data-source';

export class EnrollmentsDataSource extends BaseDataSource<IEnrollmentResponse> {
  load(userService: UserService): void {
    this.loadingSubject.next(true);

    userService
      .enrollments()
      .pipe(
        catchError(() => of([])),
        finalize(() => this.loadingSubject.next(false))
      )
      .subscribe((enrollment: IEnrollmentResponse[]) => {
        this.sourceSubject.next(enrollment);
      });
  }
}
