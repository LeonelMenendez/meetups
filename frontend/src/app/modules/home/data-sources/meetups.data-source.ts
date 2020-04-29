import { CollectionViewer } from '@angular/cdk/collections';
import { DataSource } from '@angular/cdk/table';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { catchError, finalize } from 'rxjs/operators';
import { MeetupService } from 'src/app/core/services/meetup.service';
import { IMeetupResponse } from 'src/app/shared/models/meetup';

export class MeetupsDataSource implements DataSource<IMeetupResponse> {
  private meetupSubject = new BehaviorSubject<IMeetupResponse[]>([]);
  private loadingSubject = new BehaviorSubject<boolean>(false);

  public loading$ = this.loadingSubject.asObservable();

  constructor(private meetupService: MeetupService) {}

  connect(collectionViewer: CollectionViewer): Observable<IMeetupResponse[]> {
    return this.meetupSubject.asObservable();
  }

  disconnect(collectionViewer: CollectionViewer): void {
    this.meetupSubject.complete();
    this.loadingSubject.complete();
  }

  loadCreatedMeetups(): void {
    this.loadingSubject.next(true);

    this.meetupService
      .createdMeetups()
      .pipe(
        catchError(() => of([])),
        finalize(() => this.loadingSubject.next(false))
      )
      .subscribe((meetups: IMeetupResponse[]) => this.meetupSubject.next(meetups));
  }

  loadEnrolledMeetups(): void {
    this.loadingSubject.next(true);

    this.meetupService
      .enrolledMeetups()
      .pipe(
        catchError(() => of([])),
        finalize(() => this.loadingSubject.next(false))
      )
      .subscribe((meetups: IMeetupResponse[]) => this.meetupSubject.next(meetups));
  }

  addMeetup(meetup: IMeetupResponse) {
    this.meetupSubject.next(this.meetupSubject.getValue().concat([meetup]));
  }
}
