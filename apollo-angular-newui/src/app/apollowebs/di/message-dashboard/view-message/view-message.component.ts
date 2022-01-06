import {Component, Inject, OnInit} from '@angular/core';
import {DestinationInspectionService} from "../../../../core/store/data/di/destination-inspection.service";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";

@Component({
    selector: 'app-view-message',
    templateUrl: './view-message.component.html',
    styleUrls: ['./view-message.component.css']
})
export class ViewMessageComponent implements OnInit {
    messageDetails: any
    reSubmittingMessage = false
    messageContent: any

    constructor(private diService: DestinationInspectionService, public dialogRef: MatDialogRef<any>,
                @Inject(MAT_DIALOG_DATA) public data: any) {
    }

    ngOnInit(): void {
        this.messageDetails = this.data
            this.loadFileContent()
    }
    resendFile(){
        this.diService.resendExchangeMessageFile(this.messageDetails.id)
            .subscribe(
                res=>{
                    if(res.responseCode==="00"){
                        this.diService.showSuccess(res.message,null)
                    } else {
                        this.diService.showError(res.message,null)
                    }
                }
            )
    }

    loadFileContent() {
        this.diService.loadExchangeMessageFile(this.messageDetails.id)
            .subscribe(
                res => {
                    if (res.responseCode === "00") {
                        this.messageContent = res.data
                    } else {
                        console.log("Failed to fetch message")
                    }
                }
            )

    }

}
