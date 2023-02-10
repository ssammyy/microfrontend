import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Store } from '@ngrx/store';
import { NgxSpinnerService } from 'ngx-spinner';

@Component({
  selector: 'app-none-performing-projects',
  templateUrl: './none-performing-projects.component.html',
  styleUrls: ['./none-performing-projects.component.css']
})
export class NonePerformingProjectsComponent implements OnInit {

  displayedColumns: string[] = ['projectId', 'currentStage', 'projectedStage', 'noOfWeeksDelayed','delayJustification','comments'];

  searchFormGroup!: FormGroup;

  constructor(private store$: Store<any>,
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private formBuilder: FormBuilder,
    private SpinnerService: NgxSpinnerService,) {
   }

  ngOnInit(): void {

    this.searchFormGroup = this.formBuilder.group({
      
      currentStage: ['', null],
      projectedStage: ['', null],
    });


  }

}
