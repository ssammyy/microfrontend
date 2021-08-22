import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {DestinationInspectionService} from "../../../../core/store/data/di/destination-inspection.service";

@Component({
    selector: 'app-mv-inspection-upload-file-report',
    templateUrl: './mv-inspection-upload-file-report.component.html',
    styleUrls: ['./mv-inspection-upload-file-report.component.css']
})
export class MvInspectionUploadFileReportComponent implements OnInit {
    public form: FormGroup
    public selectedFile: File
    public message: String

    constructor(public dialogRef: MatDialogRef<any>,
                private fb: FormBuilder,
                @Inject(MAT_DIALOG_DATA) public data: any,
                private diService: DestinationInspectionService) {
    }

    ngOnInit(): void {
        this.form = this.fb.group({
            comment: ['', Validators.required]
        })
    }
    closeDialog(){
        this.dialogRef.close(false)
    }
    onFileSelected(event: any) {
        console.log(event)
        if(event.target.files>0){
            this.selectedFile=event.target.files[0]
        } else {
            this.selectedFile=null
        }
    }

    saveRecord() {
        this.diService.uploadMinistryChecklist(this.selectedFile,this.form.value.comment, this.data.id)
            .subscribe(
                res=>{
                    if(res.responseCode==="00") {
                        this.dialogRef.close(true)
                    }else {
                        this.message=res.message
                    }
                }
            )

    }
}
