import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {MatTableDataSource} from "@angular/material/table";
import {SelectionModel} from "@angular/cdk/collections";

@Component({
  selector: 'app-item-selection',
  templateUrl: './item-selection.component.html',
  styleUrls: ['./item-selection.component.css']
})
export class ItemSelectionComponent implements OnInit {
  displayedColumns=['select','hsCode','details','price','description','category','compliance','sampled']
  complianceStatus =[
    {
      name: 'COMPLIANCE',
      description: 'Compliant',
    },
    {
      name: 'NON-COMPLIANCE',
      description: 'Non-Compliant',
    }
  ]
  @Output() private selectedItemsChange = new EventEmitter<any>();
  @Input() items:any[]
  @Input() categories: any[]
  @Input() selectedItems:any[]
  selectionDataSource: MatTableDataSource<any>
  selection: SelectionModel<any>
  invalidFeeSelection: Boolean;

  constructor() { }

  ngOnInit(): void {
    this.selectionDataSource = new MatTableDataSource<any>(this.items)
    this.invalidFeeSelection = true
    this.selection = new SelectionModel<any>(true, this.selectedItems);
    this.selection.changed
        .subscribe(
            res => {
              console.log("Selection changes: "+res)
              this.invalidFeeSelection = this.selection.isEmpty()
              this.selectedItemsChange.emit(this.selection.selected)
            }
        )
  }
  isAllSelected() {
    const numSelected = this.selection.selected.length;
    const numRows = this.selectionDataSource.data.length;
    return numSelected == numRows;
  }
  selectionChanged(event,row){

  }
  masterToggle() {
    this.isAllSelected() ?
        this.selection.clear() :
        this.selectionDataSource.data.forEach(row => this.selection.select(row));
  }
  changeCompliance(row,event){

  }

}
