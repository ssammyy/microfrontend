import { Component, OnInit } from '@angular/core';
import {HttpErrorResponse} from '@angular/common/http';
import {FormBuilder, FormGroup, NgForm, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {StandardDevelopmentService} from "../../../../shared/services/standard-development.service";
import {RootObject, TaskData} from '../../../../shared/models/standard-development';

@Component({
  selector: 'app-enquiry',
  templateUrl: './enquiry.component.html',
  styleUrls: ['./enquiry.component.css']
})
export class EnquiryComponent implements OnInit {

  constructor(private formBuilder: FormBuilder,
              private route: ActivatedRoute,
              private router: Router,
              private notificationService: StandardDevelopmentService
  ) { }
  public enquiryFormGroup!: FormGroup;
  ngOnInit(): void {
    this.enquiryFormGroup = this.formBuilder.group({
      requesterName: [''],
      requesterComment: [''],
      requesterCountry: [''],
      requesterEmail: [''],
      requesterInstitution: [''],
      requesterPhone: [''],
      requesterSubject: ['']
    });
  }

  sendEnquiry(): void{

    this.notificationService.makeEnquiry(this.enquiryFormGroup.value).subscribe(
      (response: RootObject) => {
        console.log(response);
        this.router.navigate(['/success']);
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }
}


