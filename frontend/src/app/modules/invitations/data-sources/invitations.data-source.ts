import { CollectionViewer } from '@angular/cdk/collections';
import { DataSource } from '@angular/cdk/table';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { catchError, finalize } from 'rxjs/operators';
import { InvitationService } from 'src/app/core/services/invitation.service';
import { IInvitationResponse } from 'src/app/shared/models/invitation';

export class InvitationsDataSource implements DataSource<IInvitationResponse> {
  private invitationSubject = new BehaviorSubject<IInvitationResponse[]>([]);
  private loadingSubject = new BehaviorSubject<boolean>(false);

  public loading$ = this.loadingSubject.asObservable();

  constructor(private invitationService: InvitationService) {}

  connect(collectionViewer: CollectionViewer): Observable<IInvitationResponse[]> {
    return this.invitationSubject.asObservable();
  }

  disconnect(collectionViewer: CollectionViewer): void {
    this.invitationSubject.complete();
    this.loadingSubject.complete();
  }

  loadInvitations(): void {
    this.loadingSubject.next(true);

    this.invitationService
      .findAll()
      .pipe(
        catchError(() => of([])),
        finalize(() => this.loadingSubject.next(false))
      )
      .subscribe((invitations: IInvitationResponse[]) => {
        this.invitationSubject.next(invitations);
      });
  }

  updateInvitation(updated: IInvitationResponse): void {
    let invitations = this.invitationSubject.getValue();
    invitations = invitations.map((invitation) =>
      invitation.id === updated.id ? updated : invitation
    );

    this.invitationSubject.next(invitations);
  }
}
