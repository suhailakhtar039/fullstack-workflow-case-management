import { ChangeDetectorRef, Component, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { CaseService } from '../../services/case.services';
import { finalize } from 'rxjs';

@Component({
  selector: 'app-case-list',
  standalone: false,
  templateUrl: './case-list.html',
  styleUrl: './case-list.css',
})
export class CaseList {
  displayedColumns: string[] = ['caseNumber', 'title', 'status'];
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
    const term = this.searchTerm.toLowerCase().trim();

    this.filteredCases = this.cases.filter((c) => {
      return c.caseNumber.toLowerCase().includes(term) || c.title.toLowerCase().includes(term);
    });
  }

  onReset() {
    this.searchTerm = '';
    this.paginator.firstPage();
    this.loadCases(0, this.pageSize);
  }
}
