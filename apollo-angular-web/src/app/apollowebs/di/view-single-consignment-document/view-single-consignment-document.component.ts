import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ApproveRejectConsignmentComponent } from './approve-reject-consignment/approve-reject-consignment.component';

@Component({
  selector: 'app-view-single-consignment-document',
  templateUrl: './view-single-consignment-document.component.html',
  styleUrls: ['./view-single-consignment-document.component.css']
})
export class ViewSingleConsignmentDocumentComponent implements OnInit {
  active = 'consignee';
  constructor(  private dialog: MatDialog) { }

  ngOnInit(): void {
  }
  approveRejectConsignment() {
    let ref = this.dialog.open(ApproveRejectConsignmentComponent, {
        data: {        }
    });
    ref.afterClosed()
        .subscribe(
            res => {
                console.log(res)
               
            }
        )
}
}
