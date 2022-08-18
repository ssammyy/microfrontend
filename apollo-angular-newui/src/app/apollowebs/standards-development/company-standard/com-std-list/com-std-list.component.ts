import { Component, OnInit } from '@angular/core';
import {Subject} from "rxjs";
import {ComJcJustificationDec, ComStandard} from "../../../../core/store/data/std/std.model";
import {Router} from "@angular/router";
import {StdComStandardService} from "../../../../core/store/data/std/std-com-standard.service";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {FormBuilder} from "@angular/forms";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-com-std-list',
  templateUrl: './com-std-list.component.html',
  styleUrls: ['./com-std-list.component.css']
})
export class ComStdListComponent implements OnInit {
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  tasks: ComStandard[] = [];
  blob: Blob;
  public actionRequest: ComStandard | undefined;
  constructor(
      private router: Router,
      private stdComStandardService:StdComStandardService,
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService,
      private formBuilder: FormBuilder
  ) { }

  ngOnInit(): void {
    this.getAllStandards();
  }
  ngOnDestroy(): void {
    this.dtTrigger.unsubscribe();
  }
  public getAllStandards(): void{
    this.SpinnerService.show();
    this.stdComStandardService.getAllStandards().subscribe(
        (response: ComStandard[])=> {
          this.SpinnerService.hide();
          this.dtTrigger.next();
          this.tasks = response;
        },
        (error: HttpErrorResponse)=>{
          this.SpinnerService.hide();
          alert(error.message);
        }
    );
  }
  viewStdFile(pdfId: number, fileName: string, applicationType: string): void {
    this.SpinnerService.show();
    this.stdComStandardService.viewCompanyStd(pdfId).subscribe(
        (dataPdf: any) => {
          this.SpinnerService.hide();
          this.blob = new Blob([dataPdf], {type: applicationType});

          // tslint:disable-next-line:prefer-const
          let downloadURL = window.URL.createObjectURL(this.blob);
          const link = document.createElement('a');
          link.href = downloadURL;
          link.download = fileName;
          link.click();
          // this.pdfUploadsView = dataPdf;
        },
        (error: HttpErrorResponse)=>{
            this.SpinnerService.hide();
            console.log(error.message);
        }
    );
  }

}
