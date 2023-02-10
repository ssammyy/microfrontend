import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Store } from '@ngrx/store';
import { NgxSpinnerService } from 'ngx-spinner';

@Component({
  selector: 'app-departmental-weekly-report',
  templateUrl: './departmental-weekly-report.component.html',
  styleUrls: ['./departmental-weekly-report.component.css']
})
export class DepartmentalWeeklyReportComponent implements OnInit {

  displayedColumns: string[] = ['slNo','indicator','units', 'achievement', 'comments'];

  searchFormGroup!: FormGroup;

  constructor(private store$: Store<any>,
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private formBuilder: FormBuilder,
    private SpinnerService: NgxSpinnerService,) {
   }

  ngOnInit(): void {

    this.searchFormGroup = this.formBuilder.group({
      
      units: ['', null],
      indicator: ['', null],
    });


  }

}