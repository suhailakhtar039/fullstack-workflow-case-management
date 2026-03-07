import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CaseService, CaseStatusHistory } from '../../services/case.services';

@Component({
  selector: 'app-case-detail',
  standalone: false,
  templateUrl: './case-detail.html',
  styleUrls: ['./case-detail.css'],
})
export class CaseDetail implements OnInit {
  caseId!: number;
  caseData: any;
  history: CaseStatusHistory[] = [];
  allowedTransitions: string[] = [];

  constructor(
    private route: ActivatedRoute,
    private caseService: CaseService,
    private cdr: ChangeDetectorRef,
  ) {}

  ngOnInit(): void {
    this.caseId = Number(this.route.snapshot.paramMap.get('id'));
    this.loadCase();
    this.loadHistory();
    this.loadTransitions();
  }

  loadCase() {
    this.caseService.getCaseById(this.caseId).subscribe((data) => (this.caseData = data));
  }

  loadHistory() {
    this.caseService.getCaseHistory(this.caseId).subscribe({
      next: (data) => {
        this.history = data;
        this.cdr.detectChanges();
        console.log('History response:', data);
      },
      error: (err) => console.error('History error:', err),
    });
  }

  loadTransitions() {
    this.caseService
      .getAllowedTransitions(this.caseId)
      .subscribe((data) => (this.allowedTransitions = data));
  }

  trackByIndex(index: number) {
    return index;
  }
}
