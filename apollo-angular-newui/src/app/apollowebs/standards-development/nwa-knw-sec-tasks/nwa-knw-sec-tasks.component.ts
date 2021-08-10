import {Component, OnDestroy, OnInit} from '@angular/core';
import {StdNwaService} from "../../../core/store/data/std/std-nwa.service";
import {NgxSpinnerService} from "ngx-spinner";
import {KnwSecTasks, NWADiSdtJustification, NWAPreliminaryDraft} from "../../../core/store/data/std/std.model";
import {HttpErrorResponse} from "@angular/common/http";
import {Subject} from "rxjs";
import swal from "sweetalert2";


@Component({
  selector: 'app-nwa-knw-sec-tasks',
  templateUrl: './nwa-knw-sec-tasks.component.html',
  styleUrls: ['./nwa-knw-sec-tasks.component.css']
})
export class NwaKnwSecTasksComponent implements OnInit,OnDestroy {
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  p = 1;
  p2 = 1;
  tasks: KnwSecTasks[] = [];
  public actionRequest: KnwSecTasks | undefined;
    public uploadedFiles: FileList;
  constructor(
      private stdNwaService: StdNwaService,
      private SpinnerService: NgxSpinnerService,
  ) { }

  ngOnInit(): void {
    this.knwtasks();

  }
  public knwtasks(): void{
    this.SpinnerService.show();
    this.stdNwaService.knwtasks().subscribe(
        (response: KnwSecTasks[])=> {
          this.tasks = response;
          this.dtTrigger.next();
          this.SpinnerService.hide();
        },
        (error: HttpErrorResponse)=>{
            this.SpinnerService.hide();
          alert(error.message);
        }
    );
  }

  public onOpenModal(task: KnwSecTasks,mode:string): void{
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle','modal');
    if (mode==='prepareJustification'){
      this.actionRequest=task;
      button.setAttribute('data-target','#prepareJustification');
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

    if (mode==='reject'){
      this.actionRequest=task;
      button.setAttribute('data-target','#reject');
    }

    // @ts-ignore
    container.appendChild(button);
    button.click();

  }
    // uploadDiSdt(): void {
    //     this.SpinnerService.show();
    //     this.stdNwaService.prepareDisDtJustification().subscribe(
    //         (response ) => {
    //             console.log(response);
    //             this.SpinnerService.hide();
    //             this.showToasterSuccess(response.httpStatus, `Request Number is ${response.body.requestNumber}`);
    //             this.onClickSaveULOADS(response.body.savedRowID)
    //             this.prepareJustificationFormGroup.reset();
    //         },
    //         (error: HttpErrorResponse) => {
    //             console.log(error.message);
    //         }
    //     );
    // }

  public uploadDiSdt(nWADiSdtJustification: NWADiSdtJustification): void{
    this.SpinnerService.show();
    this.stdNwaService.prepareDisDtJustification(nWADiSdtJustification).subscribe(
        (response) => {
          console.log(response);
          this.SpinnerService.hide();
            this.onClickSaveUPLOADS(response.body.savedRowID)
          this.knwtasks();
        },
        (error: HttpErrorResponse) => {
            this.SpinnerService.hide();
          alert(error.message);
        }
    );
  }
  public uploadPreliminaryDraft(nwaPreliminaryDraft: NWAPreliminaryDraft): void{
    this.SpinnerService.show();
    this.stdNwaService.preparePreliminaryDraft(nwaPreliminaryDraft).subscribe(
        (response: NWAPreliminaryDraft) => {
          console.log(response);
          this.SpinnerService.hide();
          this.knwtasks();
        },
        (error: HttpErrorResponse) => {
            this.SpinnerService.hide();
          alert(error.message);
        }
    );
  }
  public approvePreliminaryDraft(nwaPreliminaryDraft: NWAPreliminaryDraft): void{
    this.SpinnerService.show();
    this.stdNwaService.decisionOnPD(nwaPreliminaryDraft).subscribe(
        (response: NWAPreliminaryDraft) => {
          console.log(response);
          this.SpinnerService.hide();
          this.knwtasks();
        },
        (error: HttpErrorResponse) => {
            this.SpinnerService.hide();
          alert(error.message);
        }
    );
  }
  ngOnDestroy(): void {
    this.dtTrigger.unsubscribe();
  }
    onClickSaveUPLOADS(nwaJustificationID: string) {
        if (this.uploadedFiles.length > 0) {
            const file = this.uploadedFiles;
            const formData = new FormData();
            for (let i = 0; i < file.length; i++) {
                console.log(file[i]);
                formData.append('docFile', file[i], file[i].name);
            }

            this.SpinnerService.show();
            this.stdNwaService.uploadDIFileDetails(nwaJustificationID, formData).subscribe(
                (data: any) => {
                    this.SpinnerService.hide();
                    this.uploadedFiles = null;
                    console.log(data);
                    swal.fire({
                        title: 'Justification Prepared.',
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

}
