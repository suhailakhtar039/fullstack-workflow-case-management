import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CaseService } from '../../services/case.services';

@Component({
  selector: 'app-case-detail',
  standalone: false,
  templateUrl: './case-detail.html',
  styleUrl: './case-detail.css',
})
export class CaseDetail implements OnInit {
  caseId!: number;
  caseData: any;
  history: any[] = [];
  allowedTransitions: string[] = [];

  constructor(
    private route: ActivatedRoute,
    private caseService: CaseService,
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
    this.caseService.getCaseHistory(this.caseId).subscribe((data) => (this.history = data));
  }

  loadTransitions() {
    this.caseService
      .getAllowedTransitions(this.caseId)
      .subscribe((data) => (this.allowedTransitions = data));
  }
}
