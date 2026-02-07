import { ChangeDetectorRef, Component, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { CaseService } from '../../services/case.services';

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

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  constructor(
    private caseService: CaseService,
    private cdr: ChangeDetectorRef,
  ) {}

  ngOnInit() {
    this.loadCases(0, this.pageSize);
  }

  loadCases(page: number, size: number) {
    this.caseService.getCases(page, size).subscribe((response) => {
      console.log('response = ' + response);
      console.log('response content = ' + response.content);
      console.log('total elements: ' + response.totalElements);
      this.cases = response.content;
      this.totalElements = response.totalElements;
      this.cdr.detectChanges();
    });
  }

  onPageChange(event: any) {
    this.loadCases(event.pageIndex, event.pageSize);
  }
}
