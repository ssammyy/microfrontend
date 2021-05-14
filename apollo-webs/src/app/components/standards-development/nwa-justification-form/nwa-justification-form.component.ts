import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, NgForm, Validators} from "@angular/forms";
import {
  KNWCommittee,
  KNWDepartment, KnwSecTasks,
  NWAJustification
} from "../../../shared/models/standard-development";
import {HttpErrorResponse} from "@angular/common/http";
import {StdDevelopmentNwaService} from "../../../shared/services/std-development-nwa.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-nwa-justification-form',
  templateUrl: './nwa-justification-form.component.html',
  styleUrls: ['./nwa-justification-form.component.css']
})
export class NwaJustificationFormComponent implements OnInit {

  public nwaDepartments !: KNWDepartment[] ;
  public nwaCommittees !: KNWCommittee[] ;
  public prepareJustificationFormGroup!: FormGroup;
  public knwsectasks !: KnwSecTasks[] ;
  // knwsectasks: KnwSecTasks[] = [];

  constructor(
    private router: Router,
    private formBuilder: FormBuilder,
    private stdDevelopmentNwaService: StdDevelopmentNwaService
  ) { }

  ngOnInit(): void {
    this.getKNWDepartments();
    this.getKNWCommittee();
    this.knwtasks();

    this.prepareJustificationFormGroup = this.formBuilder.group({
      meetingDate:['', Validators.required],
      knw:['', Validators.required],
      sl:['', Validators.required],
      requestNumber:['', Validators.required],
      department:['', Validators.required],
      requestedBy:['', Validators.required],
      issuesAddressed:['', Validators.required],
      knwAcceptanceDate:['', Validators.required],
      referenceMaterial:['', Validators.required],
      taskId: ['', Validators.required],
      knwSecretary: ['', Validators.required]
      // postalAddress: ['', [Validators.required, Validators.pattern('P.O.BOX [0-9]{5}')]]
    });

  }

  get formPrepareJustification(): any{
    return this.prepareJustificationFormGroup.controls;
  }

  public getKNWDepartments(): void{
    this.stdDevelopmentNwaService.getKNWDepartments().subscribe(
      (response: KNWDepartment[])=> {
        this.nwaDepartments = response;
      },
      (error: HttpErrorResponse)=>{
        alert(error.message);
      }
    );
  }

  public getKNWCommittee(): void{
    this.stdDevelopmentNwaService.getKNWCommittee().subscribe(
      (response: KNWCommittee[])=> {
        this.nwaCommittees = response;
      },
      (error: HttpErrorResponse)=>{
        alert(error.message);
      }
    );
  }

  saveJustification(): void{

    this.stdDevelopmentNwaService.prepareJustification(this.prepareJustificationFormGroup.value).subscribe(
      (response:NWAJustification) =>{
        console.log(response);
        this.prepareJustificationFormGroup.reset();
      },
      (error:HttpErrorResponse) =>{
        alert(error.message);
      }
    );
  }

  public knwtasks(): void{
    this.stdDevelopmentNwaService.knwtasks().subscribe(
      (response: KnwSecTasks[])=> {
        this.knwsectasks = response;
      },
      (error: HttpErrorResponse)=>{
        alert(error.message);
      }
    );
  }

  // public saveJustification(saveJustification: NgForm): void{
  //   this.stdDevelopmentNwaService.prepareJustification(saveJustification.value).subscribe(
  //     (response:StandardTasks) =>{
  //       console.log(response);
  //     },
  //     (error:HttpErrorResponse) =>{
  //       alert(error.message);
  //     }
  //   )
  // }



}
