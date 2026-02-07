import { ChangeDetectorRef, Component, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
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
  totalElements = 0;
  pageSize = 10;

  isLoading = false;
  hasError = false;
  errorMessage = '';
  searchTerm = '';
  filteredCases: any[] = [];

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  constructor(
    private caseService: CaseService,
    private cdr: ChangeDetectorRef,
  ) {}

  ngOnInit() {
    this.loadCases(0, this.pageSize);
  }

  loadCases(page: number, size: number) {
    console.log('LOAD START');
    this.isLoading = true;

    this.caseService
      .getCases(page, size)
      .pipe(
        finalize(() => {
          console.log('FINALIZE CALLED isLoading: ', this.isLoading);
          this.isLoading = false;
          this.cdr.markForCheck();
        }),
      )
      .subscribe({
        next: (response: any) => {
          console.log('NEXT CALLED isLoading: ', this.isLoading);
          this.cases = response.content;
          this.filteredCases = response.content;
          this.totalElements = response.totalElements;
          this.applyFilters();
        },
        error: (error) => {
          console.log('ERROR CALLED', error);
          this.hasError = true;
          this.errorMessage = 'Failed to load cases!';
          this.cases = [];
          this.totalElements = 0;
        },
      });
  }

  onPageChange(event: any) {
    // console.log('check in page change');
    this.pageSize = event.pageSize;
    this.loadCases(event.pageIndex, event.pageSize);
  }

  searchCaseAndTitle() {
    this.paginator.firstPage();
    this.applyFilters();
  }

  applyFilters() {
    const term = this.searchTerm.toLowerCase().trim();

    this.filteredCases = this.cases.filter((c) => {
      const matchesSearch =
        !term ||
        c.caseNumber?.toLowerCase().includes(term) ||
        c.title?.toLowerCase().includes(term);

      const matchesStatus = !this.selectedStatus || c.status === this.selectedStatus;

      const matchesMyCases = !this.showMyCases || c.createdBy === this.currentUsername;

      return matchesSearch && matchesStatus && matchesMyCases;
    });
  }

  onReset() {
    this.searchTerm = '';
    this.selectedStatus = '';
    this.showMyCases = false;

    this.paginator.firstPage();
    this.loadCases(0, this.pageSize);
  }
}
