import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Store } from '@ngrx/store';
import { NgxSpinnerService } from 'ngx-spinner';

@Component({
  selector: 'app-individual-weekly-report',
  templateUrl: './individual-weekly-report.component.html',
  styleUrls: ['./individual-weekly-report.component.css']
})
export class IndividualWeeklyReportComponent implements OnInit {

  displayedColumns: string[] = ['slNo','hrNumber','name','section','deliverables','expectedOutput','noOfTcMeeting','noOfDissemination','date'];

  searchFormGroup!: FormGroup;

  constructor(private store$: Store<any>,
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private formBuilder: FormBuilder,
    private SpinnerService: NgxSpinnerService,) {
   }

  ngOnInit(): void {

    this.searchFormGroup = this.formBuilder.group({
      
      name: ['', null],
      section: ['', null],
      
    });


  }

}

