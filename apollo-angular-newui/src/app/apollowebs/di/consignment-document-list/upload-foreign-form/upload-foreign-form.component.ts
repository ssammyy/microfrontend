import {Component, OnInit} from '@angular/core';
import {DestinationInspectionService} from "../../../../core/store/data/di/destination-inspection.service";
import {MatDialogRef} from "@angular/material/dialog";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";

@Component({
    selector: 'app-upload-foreign-form',
    templateUrl: './upload-foreign-form.component.html',
    styleUrls: ['./upload-foreign-form.component.css']
})
export class UploadForeignFormComponent implements OnInit {
    selectedFile: File
    message: String
    form: FormGroup
    constructor(private fb: FormBuilder,private dialogRef: MatDialogRef<any>, private diService: DestinationInspectionService) {
    }

    ngOnInit(): void {
        this.form=this.fb.group({
            fileType: ["",Validators.required]
        })
    }

    uploadForeignCoROrCor(event: any) {
        console.log(event.target.files.length)
        if (event.target.files.length > 0) {
            this.selectedFile = event.target.files[0]
        } else {
            this.selectedFile = null
        }
    }

    saveForeignDocument() {
        this.diService.uploadForeignDocuments(this.selectedFile, this.form.value.fileType,"coc")
            .subscribe(
                res => {
                    if (res.responseCode === "000") {
                        this.dialogRef.close(true)
                    } else {
                        this.message = res.message
                    }
                },
                error => {
                    this.message = error.message
                }
            )

    }
}
