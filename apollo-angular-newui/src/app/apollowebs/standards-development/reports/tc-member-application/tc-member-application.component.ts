import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Store } from '@ngrx/store';
import { NgxSpinnerService } from 'ngx-spinner';

@Component({
  selector: 'app-tc-member-application',
  templateUrl: './tc-member-application.component.html',
  styleUrls: ['./tc-member-application.component.css']
})
export class TcMemberApplicationComponent implements OnInit {

  displayedColumns: string[] = ['slNo', 'name', 'organization', 'category', 'division', 'qualification', 'relevantExperience', 'email','attachedCv', 'remarks'];

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
      organization: ['', null],
      category: ['', null],
      qualification: ['', null],
    });


  }

}

