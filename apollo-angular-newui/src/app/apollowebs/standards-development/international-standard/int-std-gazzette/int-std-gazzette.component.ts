import { Component, OnInit } from '@angular/core';
import {Subject} from "rxjs";
import {ISHopTASKS, ISHosSicTASKS} from "../../../../core/store/data/std/std.model";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {StdIntStandardService} from "../../../../core/store/data/std/std-int-standard.service";
import {HttpErrorResponse} from "@angular/common/http";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../../core/store/data/std/notification.service";

@Component({
  selector: 'app-int-std-gazzette',
  templateUrl: './int-std-gazzette.component.html',
  styleUrls: ['./int-std-gazzette.component.css']
})
export class IntStdGazzetteComponent implements OnInit {
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  tasks: ISHosSicTASKS[] = [];
  public uploadedFiles:  FileList;
  public actionRequest: ISHosSicTASKS | undefined;
  public prepareGazetteNotice!: FormGroup;
  constructor(
      private formBuilder: FormBuilder,
      private stdIntStandardService : StdIntStandardService,
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService
  ) { }

  ngOnInit(): void {
    this.getHoSiCTasks();
    this.prepareGazetteNotice = this.formBuilder.group({
      description: ['', Validators.required],
      taskId: [],
      iSNumber: []

    });
  }
  ngOnDestroy(): void {
    this.dtTrigger.unsubscribe();
  }
  get formPrepareGN(): any {
    return this.prepareGazetteNotice.controls;
  }
  showToasterError(title:string,message:string){
    this.notifyService.showError(message, title)

  }
  showToasterSuccess(title:string,message:string){
    this.notifyService.showSuccess(message, title)

  }
  public getHoSiCTasks(): void{
    this.SpinnerService.show();
    this.stdIntStandardService.getHoSiCTasks().subscribe(
        (response: ISHosSicTASKS[])=> {
          this.tasks = response;
          this.SpinnerService.hide();
          this.dtTrigger.next();
        },
        (error: HttpErrorResponse)=>{
          this.SpinnerService.hide();
          alert(error.message);
        }
    );
  }
  public onOpenModal(task: ISHosSicTASKS,mode:string): void{
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle','modal');
    if (mode==='upload'){
      this.actionRequest=task;
      button.setAttribute('data-target','#uploadModal');
    }
    if (mode==='update'){
      this.actionRequest=task;
      button.setAttribute('data-target','#updateModal');
    }
    // @ts-ignore
    container.appendChild(button);
    button.click();

  }
  // Upload Standard
  onUpload(): void {
    this.SpinnerService.show();
    //console.log(this.prepareStandardFormGroup.value);
    this.stdIntStandardService.uploadGazetteNotice(this.prepareGazetteNotice.value).subscribe(
        (response ) => {
          console.log(response);
          this.SpinnerService.hide();
          this.showToasterSuccess('Success', `Gazette Notice Uploaded`);
          this.prepareGazetteNotice.reset();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          this.showToasterError('Error', `Gazette Notice Was Not uploaded`);
          console.log(error.message);
        }
    );
  }
  onUpdate(): void {
    this.SpinnerService.show();
    //console.log(this.prepareStandardFormGroup.value);
    this.stdIntStandardService.updateGazetteDate(this.prepareGazetteNotice.value).subscribe(
        (response ) => {
          console.log(response);
          this.SpinnerService.hide();
          this.showToasterSuccess('Success', `Gazettement Date Updated`);
          this.prepareGazetteNotice.reset();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          this.showToasterError('Error', `Gazettement Date Was Not Updated`);
          console.log(error.message);
        }
    );
  }
}
