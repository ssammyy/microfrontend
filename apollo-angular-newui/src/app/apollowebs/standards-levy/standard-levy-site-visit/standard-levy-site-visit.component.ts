import { Component, OnInit } from '@angular/core';
import {Subject} from "rxjs";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {VisitTask} from "../../../core/store/data/levy/levy.model";
import {Router} from "@angular/router";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../core/store/data/std/notification.service";
import {LevyService} from "../../../core/store/data/levy/levy.service";
import {KnwSecTasks} from "../../../core/store/data/std/std.model";
import {HttpErrorResponse} from "@angular/common/http";
import swal from "sweetalert2";

@Component({
  selector: 'app-standard-levy-site-visit',
  templateUrl: './standard-levy-site-visit.component.html',
  styleUrls: ['./standard-levy-site-visit.component.css']
})
export class StandardLevySiteVisitComponent implements OnInit {
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  displayTable: boolean = false;
  blob: Blob;
  tasks: VisitTask[] = [];
  public actionRequest: VisitTask | undefined;
  public uploadedFiles:  FileList;
  public prepareReportFormGroup!: FormGroup;
  constructor(
      private router: Router,
      private formBuilder: FormBuilder,
      private levyService: LevyService,
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService
  ) { }

  ngOnInit(): void {
    this.getScheduledVisits();

      this.prepareReportFormGroup = this.formBuilder.group({
          visitDate: ['', Validators.required],
          purpose: ['', Validators.required],
          personMet: ['', Validators.required],
          actionTaken: ['', Validators.required],
          taskId: [],
          visitID: []

      });
  }
    showToasterSuccess(title:string,message:string){
        this.notifyService.showSuccess(message, title)

    }
    showToasterError(title:string,message:string){
        this.notifyService.showError(message, title)

    }
    showToasterWarning(title:string,message:string){
        this.notifyService.showWarning(message, title)

    }
    get formPrepareReport(): any {
        return this.prepareReportFormGroup.controls;
    }
  public getScheduledVisits(): void{
    this.SpinnerService.show();
    this.levyService.getScheduledVisits().subscribe(
        (response: VisitTask[])=> {
          this.tasks = response;
          this.dtTrigger.next();
          this.displayTable = true;
          this.SpinnerService.hide();
        },
        (error: HttpErrorResponse)=>{
          this.SpinnerService.hide();
          alert(error.message);
        }
    );
  }
    saveReport(): void {
        this.SpinnerService.show();
        this.levyService.saveSiteVisitReport(this.prepareReportFormGroup.value).subscribe(
            (response ) => {
                console.log(response);
                this.SpinnerService.hide();
                this.onClickSaveUploads(response.body.savedRowID)
                this.prepareReportFormGroup.reset();
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                console.log(error.message);
            }
        );
    }
    onClickSaveUploads(reportFileID: string) {
        if (this.uploadedFiles.length > 0) {
            const file = this.uploadedFiles;
            const formData = new FormData();
            for (let i = 0; i < file.length; i++) {
                console.log(file[i]);
                formData.append('docFile', file[i], file[i].name);
            }
            this.SpinnerService.show();
            this.levyService.uploadFileDetails(reportFileID, formData).subscribe(
                (data: any) => {
                    this.SpinnerService.hide();
                    this.uploadedFiles = null;
                    console.log(data);
                    swal.fire({
                        title: 'Report Prepared.',
                        buttonsStyling: false,
                        customClass: {
                            confirmButton: 'btn btn-success form-wizard-next-btn ',
                        },
                        icon: 'success'
                    });
                },
            );
        }

    }

    public onOpenModal(task: VisitTask,mode:string): void{
        const container = document.getElementById('main-container');
        const button = document.createElement('button');
        button.type = 'button';
        button.style.display = 'none';
        button.setAttribute('data-toggle','modal');
        if (mode==='createSiteReport'){
            this.actionRequest=task;
            button.setAttribute('data-target','#createSiteReport');
        }
        if (mode==='rejectJustification'){
            this.actionRequest=task;
            button.setAttribute('data-target','#rejectJustification');
        }
        if (mode==='prepDiSdt'){
            this.actionRequest=task;
            button.setAttribute('data-target','#prepDiSdt');
        }

        if (mode==='prepPd'){
            this.actionRequest=task;
            button.setAttribute('data-target','#prepPd');
        }
        if (mode==='approvePd'){
            this.actionRequest=task;
            button.setAttribute('data-target','#approvePd');
        }

        if (mode==='rejectPd'){
            this.actionRequest=task;
            button.setAttribute('data-target','#rejectPd');
        }

        // @ts-ignore
        container.appendChild(button);
        button.click();

    }

}
