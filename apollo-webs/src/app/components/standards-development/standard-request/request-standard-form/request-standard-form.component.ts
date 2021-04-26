import { Component, OnInit } from '@angular/core';
import {Department, Product, StandardRequest, TechnicalCommittee} from "../standardrequest";
import {HttpErrorResponse} from "@angular/common/http";
import {StandardRequestService} from "../../../../shared/services/standard-request.service";
import {FormBuilder, FormGroup, NgForm, Validators} from "@angular/forms";

@Component({
  selector: 'app-request-standard-form',
  templateUrl: './request-standard-form.component.html',
  styleUrls: ['./request-standard-form.component.css']
})
export class RequestStandardFormComponent implements OnInit {
  public products : Product[] | undefined;
  public departments: Department[] | undefined;
  public committees: TechnicalCommittee[] | undefined;

  public stdRequestFormGroup!: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private standardRequestService:StandardRequestService
  ) { }

  ngOnInit(): void {
    this.getStandards();
    this.getDepartments();
    this.getTechnicalCommittee();

    this.stdRequestFormGroup = this.formBuilder.group({
      name:['', Validators.required],
      phone:['', Validators.required],
      email:['', Validators.required],
      tc_id:['', Validators.required],
      department_id:['', Validators.required],
      productCategory:['', Validators.required],
      product_category:['', Validators.required]
      // postalAddress: ['', [Validators.required, Validators.pattern('P.O.BOX [0-9]{5}')]]
    });
  }

  get formStdRequest(): any{
    return this.stdRequestFormGroup.controls;
  }
  public getStandards(): void{
    this.standardRequestService.getStandards().subscribe(
      (response: Product[])=> {
        this.products = response;
      },
      (error: HttpErrorResponse)=>{
        alert(error.message);
      }
    );
  }
  public getDepartments(): void{
    this.standardRequestService.getDepartments().subscribe(
      (response: Department[])=> {
        this.departments = response;
      },
      (error: HttpErrorResponse)=>{
        alert(error.message);
      }
    );
  }

  public getTechnicalCommittee(): void{
    this.standardRequestService.getTechnicalCommittee().subscribe(
      (response: TechnicalCommittee[])=> {
        this.committees = response;
      },
      (error: HttpErrorResponse)=>{
        alert(error.message);
      }
    );
  }

   saveStandard(): void{

    this.standardRequestService.addStandardRequest(this.stdRequestFormGroup.value).subscribe(
      (response:StandardRequest) =>{
        console.log(response);
      },
      (error:HttpErrorResponse) =>{
        alert(error.message);
      }
    )
  }

  onSelectProductCategory(value: any): any {
    return
  }
}
