import { ChangeDetectorRef, Component, ViewChild } from '@angular/core';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { CaseService } from '../../services/case.services';
import { finalize } from 'rxjs';
import { apply } from '@angular/forms/signals';

@Component({
  selector: 'app-case-list',
  standalone: false,
  templateUrl: './case-list.html',
  styleUrl: './case-list.css',
})
export class CaseList {
  displayedColumns: string[] = ['caseNumber', 'title', 'status', 'actions'];
  caseStatuses: string[] = ['DRAFT', 'IN_REVIEW', 'FILED', 'APPROVED', 'REJECTED'];

  selectedStatus: string = '';
  showMyCases = false;
  currentUsername = 'admin'; // TEMP: replace later from token
  cases: any[] = [];
  currentPage = 0;
  totalElements = 0;
  pageSize = 10;

  isLoading = false;
  hasError = false;
  errorMessage = '';
  searchTerm = '';

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  constructor(
    private caseService: CaseService,
    private cdr: ChangeDetectorRef,
  ) {}

  ngOnInit() {
    this.loadCases(0, this.pageSize);
  }

  loadCases(page: number, size: number) {
    console.log('loadCases START');
    this.isLoading = true;

    this.caseService
      .getCases(page, size, this.searchTerm, this.selectedStatus, this.showMyCases)
      .pipe(
        finalize(() => {
          this.isLoading = false;
          this.cdr.markForCheck();
        }),
      )
      .subscribe({
        next: (res: any) => {
          this.cases = res.content.map((c: any) => ({
            ...c,
            allowedTransitions: [],
          }));
          this.totalElements = res.totalElements;
          this.cases.forEach((c, index) => {
            this.caseService.getAllowedTransitions(c.id).subscribe((transitions: string[]) => {
              const updatedCase = {
                ...c,
                allowedTransitions: transitions,
              };
              this.cases = [
                ...this.cases.slice(0, index),
                updatedCase,
                ...this.cases.slice(index + 1),
              ];
              this.cdr.markForCheck();
              // c.allowedTransitions = transitions;
            });
          });
        },
        error: (err) => {
          this.hasError = true;
          this.errorMessage = 'Failed to load cases';
          this.cases = [];
          this.totalElements = 0;
        },
      });
  }

  onPageChange(event: PageEvent) {
    // console.log('check in page change');
    this.currentPage = event.pageIndex;
    this.pageSize = event.pageSize;
    this.loadCases(this.currentPage, this.pageSize);
  }

  onSearch() {
    this.paginator.firstPage();
    this.loadCases(0, this.pageSize);
  }

  onReset() {
    this.searchTerm = '';
    this.selectedStatus = '';
    this.showMyCases = false;

    this.paginator.firstPage();
    this.loadCases(0, this.pageSize);
  }

  transition(caseId: number, status: string) {
    this.caseService.transitionCase(caseId, status).subscribe({
      next: () => {
        this.loadCases(this.currentPage, this.pageSize);
      },
      error: () => {
        console.error('Transition failed');
      },
    });
  }

  formatLabel(status: string): string {
    return status
      .replace('_', ' ')
      .toLowerCase()
      .replace(/\b\w/g, (c) => c.toUpperCase());
  }
}
