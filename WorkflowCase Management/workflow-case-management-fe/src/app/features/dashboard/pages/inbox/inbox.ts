import { ChangeDetectorRef, Component } from '@angular/core';
import { InboxService } from '../../services/inbox.service';
import { finalize } from 'rxjs';
import { CaseService } from '../../services/case.services';

@Component({
  selector: 'app-inbox',
  standalone: false,
  templateUrl: './inbox.html',
  styleUrl: './inbox.css',
})
export class Inbox {
  displayedColumns: string[] = ['caseNumber', 'title', 'currentStatus', 'pendingRole', 'actions'];

  cases: any[] = [];
  isLoading = false;
  hasError = false;
  errorMessage = '';
  actionInProgress: number | null = null;

  constructor(
    private inboxService: InboxService,
    private cdr: ChangeDetectorRef,
    private caseService: CaseService,
  ) {}

  ngOnInit() {
    this.loadInbox();
  }

  loadInbox() {
    this.isLoading = true;
    this.hasError = false;

    this.inboxService
      .getMyPendingTasks()
      .pipe(
        finalize(() => {
          this.isLoading = false;
          this.cdr.markForCheck();
        }),
      )
      .subscribe({
        next: (res) => {
          this.cases = res;
        },
        error: (res) => {
          this.hasError = true;
          ((this.errorMessage = 'Failed to load inbox'), (this.cases = []));
        },
      });
  }

  performAction(row: any) {
    if (!confirm(`Proceed with action: ${row.targetStatus}?`)) {
      return;
    }

    this.actionInProgress = row.caseId;

    this.caseService
      .transitionCase(row.caseId, row.targetStatus)
      .pipe(finalize(() => (this.actionInProgress = null)))
      .subscribe({
        next: () => this.loadInbox(),
        error: (err) => {
          alert(err?.error?.message || 'Action failed');
        },
      });
  }
}
