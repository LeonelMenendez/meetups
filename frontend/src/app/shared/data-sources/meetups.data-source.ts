import { of } from 'rxjs';
import { catchError, finalize } from 'rxjs/operators';
import { UserService } from 'src/app/core/services/user.service';
import { IMeetupResponse } from 'src/app/shared/models/meetup';

import { BaseDataSource } from './base.data-source';

export class MeetupsDataSource extends BaseDataSource<IMeetupResponse> {
  load(userService: UserService): void {
    this.loadingSubject.next(true);

    userService
      .createdMeetups()
      .pipe(
        catchError(() => of([])),
        finalize(() => this.loadingSubject.next(false))
      )
      .subscribe((meetups: IMeetupResponse[]) => this.sourceSubject.next(meetups));
  }
}
