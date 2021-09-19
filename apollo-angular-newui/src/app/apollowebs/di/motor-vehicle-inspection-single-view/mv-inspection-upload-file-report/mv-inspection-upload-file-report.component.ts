import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {DestinationInspectionService} from "../../../../core/store/data/di/destination-inspection.service";
import {file} from "googleapis/build/src/apis/file";

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
        let files=event.target.files
        if(files.length>0){
            this.selectedFile=files[0]
        } else {
            this.selectedFile=null
        }
        console.log(this.selectedFile)
    }

    saveRecord() {
        if(this.selectedFile) {
            this.diService.uploadMinistryChecklist(this.selectedFile, this.form.value.comment, this.data.id)
                .subscribe(
                    res => {
                        if (res.responseCode === "00") {
                            this.dialogRef.close(true)
                        } else {
                            this.message = res.message
                        }
                    }
                )
        }else {
            this.message="Please select file to upload"
        }

    }
}
