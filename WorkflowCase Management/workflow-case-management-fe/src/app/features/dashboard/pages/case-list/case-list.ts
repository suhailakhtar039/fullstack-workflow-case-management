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
  displayedColumns: string[] = ['caseNumber', 'title', 'status'];
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
          this.cases = res.content;
          this.totalElements = res.totalElements;
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
}
