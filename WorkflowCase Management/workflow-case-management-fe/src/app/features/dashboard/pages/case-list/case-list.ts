import { Component } from '@angular/core';

@Component({
  selector: 'app-case-list',
  standalone: false,
  templateUrl: './case-list.html',
  styleUrl: './case-list.css',
})
export class CaseList {
  displayedColumns: string[] = ['caseNumber', 'title', 'status'];
}
