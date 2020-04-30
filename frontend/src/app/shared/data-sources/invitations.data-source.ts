import { of } from 'rxjs';
import { catchError, finalize } from 'rxjs/operators';
import { InvitationService } from 'src/app/core/services/invitation.service';
import { BaseDataSource } from 'src/app/shared/data-sources/base.data-source';
import { IInvitationResponse } from 'src/app/shared/models/invitation';

export class InvitationsDataSource extends BaseDataSource<IInvitationResponse> {
  load(invitationService: InvitationService): void {
    this.loadingSubject.next(true);

    invitationService
      .findAll()
      .pipe(
        catchError(() => of([])),
        finalize(() => this.loadingSubject.next(false))
      )
      .subscribe((invitations: IInvitationResponse[]) => {
        this.sourceSubject.next(invitations);
      });
  }
}
