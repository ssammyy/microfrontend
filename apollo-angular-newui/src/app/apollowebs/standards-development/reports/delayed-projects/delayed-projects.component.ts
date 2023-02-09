import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Store } from '@ngrx/store';
import { NgxSpinnerService } from 'ngx-spinner';

@Component({
  selector: 'app-delayed-projects',
  templateUrl: './delayed-projects.component.html',
  styleUrls: ['./delayed-projects.component.css']
})
export class DelayedProjectsComponent implements OnInit {

  displayedColumns: string[] = ['slNo','ksNo','date', 'currentStage', 'delayReason','remedialPan'];

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
      date: ['', null],
    });


  }

}