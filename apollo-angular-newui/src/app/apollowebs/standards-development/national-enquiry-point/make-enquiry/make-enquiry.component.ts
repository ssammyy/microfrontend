import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {NepPointService} from "../../../../core/store/data/std/nep-point.service";
import {RootObject} from "../../../../core/store/data/std/std.model";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-make-enquiry',
  templateUrl: './make-enquiry.component.html',
  styleUrls: ['./make-enquiry.component.css']
})
export class MakeEnquiryComponent implements OnInit {

  constructor(private formBuilder: FormBuilder,
              private route: ActivatedRoute,
              private router: Router,
              private notificationService: NepPointService) { }
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