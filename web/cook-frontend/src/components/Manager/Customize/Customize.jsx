import React from "react";
import Container from 'react-bootstrap/Container';
import Card from 'react-bootstrap/Card';
import https from 'https';
import axios from 'axios';
import Cookies from 'universal-cookie';
import Row from 'react-bootstrap/Row';
import { ChromePicker } from 'react-color';
import Modal from 'react-bootstrap/Modal';

/*this is the customize component for the manager
view. The customization info is prefilled from the pull from
the database made on Manager. 

Each field has an edit button  when clicked changes 
the 'sectionEdit' state to that section.

renderInfo is called which either creates a form to edit for the
section or renders what is there from the database.*/

class Customize extends React.Component{
    constructor(props) {     
        super(props);
        
        const cookies = new Cookies();
        this.state = {
          customizeInfo: [],
          fonts: [ 'Oswald', 'Raleway', 'Open Sans', 'Lato', 'Pt Sans', 'Lora', 'Montserrat', 'Playfair Display', 'Benchnine', 'Merriweather'],
          sectionEdit: "",
          show:false,
          font: this.props.info.font,
          primary: this.props.info.primary_color ,
          secondary: this.props.info.secondary_color,
          tertiary: this.props.info.tertiary_color,
          temp_primary: this.props.info.primary_color,
          temp_secondary: this.props.info.secondary_color,
          temp_tertiary:this.props.info.tertiary_color,
          font_color : '#111111',
          temp_font_color: '#111111',
          file: null,
          fileName:"Choose file",
          restaurant_id :cookies.get("mystaff").restaurant_id,
          token:cookies.get('mytoken')
        };

    this.onChange = this.onChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
    this.handleShow = this.handleShow.bind(this);

  }
    /*
      On change of the edit field- update the field in the
      state so we can send it to the database.
    */
  onChange = (e) => {

      //colors
      if(this.state.sectionEdit ==="Primary")
      {  this.setState({ 'temp_primary':  e.hex});
      }
      else if(this.state.sectionEdit ==="Seconday")
      {
        this.setState({ 'temp_secondary':  e.hex});
      }
      else if(this.state.sectionEdit ==="Tertiary")
      {
        this.setState({ 'temp_tertiary':  e.hex});
      }
      else if(this.state.sectionEdit ==="Font Color")
      {
        this.setState({ 'temp_font_color':  e.hex});
      }
      
    }

      onChangeFile = (e) => {
        console.log(e.target.files)
        //change the file name
        this.setState({ fileName: e.target.files[0].name });
        //get our image into blob format
        this.loadXHR(e.target.files[0]).then(function(blob) {
          // here the image is a blob
          this.setState({ file: blob });
        }.bind(this));
      }


  /* Used for connecting to Customization in database */
  handleSubmit(event) {
    console.log(this.state);
    this.editForm("");

    //event.preventDefault();
    
    /*https://jasonwatmore.com/post/2020/02/01/react-fetch-http-post-request-examples is where I'm pulling this formatting from.*/

    axios({
      method: 'POST',
      url:  process.env.REACT_APP_DB +'/restaurant/update/',
      data: 'restaurant_id='+this.state.restaurant_id+'&name='+this.state.name+
      '&address='+this.state.address+'&phone='+this.state.phone+
      '&opening='+this.state.open+'&closing='+this.state.close,
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
        'Authorization': 'Bearer ' + this.state.token
      },
      httpsAgent: new https.Agent({  
        rejectUnauthorized: false,
      }),
    })
    .then(async response => {
      await response;

      if (response.status !== 200) {this.handleShow(false);}
      else {this.handleShow(true, "changed");}
    })
    .catch(error => {
      this.handleShow(false);
      console.error("There was an error!", error);
    });


}

  /* Used to show the correct alert after hitting save item */
  handleShow(success, message) {
    if (success) {
      this.setState({response: "Successfully "+message+"!"});
      this.setState({alertVariant: 'success'});
    }
    else {
      this.setState({response: 'Failed to update'})
      this.setState({alertVariant: 'danger'});
    }

    this.setState({show: true});
  }

//change the category of which is being edited
editForm = (category) => {
    this.setState({
      sectionEdit: category
  });
}

change(event){
  this.setState({value: event.target.value});
}
//convert image to blob from https://stackoverflow.com/questions/42471755/convert-image-into-blob-using-javascript
loadXHR(url) {
    return new Promise(function(resolve, reject) {
        try {
            var xhr = new XMLHttpRequest();
            xhr.open("GET", url);
            xhr.responseType = "blob";
            xhr.onerror = function() {reject("Network error.")};
            xhr.onload = function() {
                if (xhr.status === 200) {resolve(xhr.response)}
                else {reject("Loading error:" + xhr.statusText)}
            };
            xhr.send();
        }
        catch(err) {reject(err.message)}
    });
}
  handleModalClose = () => this.setState({ModalShow: false});
  handleModalShow = () => this.setState({ModalShow: true});

renderInfo(){
    return (
          <>
            <Modal show={this.state.ModalShow} onHide={this.handleModalClose} centered>
              <Modal.Header closeButton>
                <Modal.Title>{this.state.sectionEdit}</Modal.Title>
              </Modal.Header>

                    {/*primary*/}
                    {this.state.sectionEdit === "Primary" && 
                    <Modal.Body style={{backgroundColor:this.state.temp_primary}}>
                      <div className="container">
                        <div className="row">
                          <Container>
                              <ChromePicker
                                color={ this.state.temp_primary }
                                onChangeComplete={this.onChange }
                              />
                              <br/>
                            </Container>
                          </div>
                        </div>
                      </Modal.Body>
                    }
                    {/*secondary*/}
                    {this.state.sectionEdit === "Secondary" && 
                    <Modal.Body style={{backgroundColor:this.state.temp_secondary}}>
                      <div className="container">
                        <div className="row">
                          <Container>
                            <ChromePicker
                              color={ this.state.temp_secondary }
                              onChangeComplete={this.onChange }
                            />
                   
                          </Container>
                        </div>
                        </div>
                      </Modal.Body>
                    }
                    {/*tertiary*/}
                    {this.state.sectionEdit === "Tertiary" && 
                    <Modal.Body style={{backgroundColor:this.state.temp_tertiary}}>
                      <div className="container">
                        <div className="row">
                          <Container>
                            <ChromePicker
                              color={ this.state.temp_tertiary }
                              onChangeComplete={this.onChange }
                            />
                          </Container>
                         </div>
                        </div>
                      </Modal.Body>
                      }
                    {/*font color*/}
                    {this.state.sectionEdit === "Font Color" && 
                    <Modal.Body style={{backgroundColor:this.state.temp_font_color}}>
                      <div className="container">
                        <div className="row">
                          <Container>
                            <ChromePicker
                              color={ this.state.temp_font_color }
                              onChangeComplete={this.onChange }
                            />
                          </Container>
                          </div>
                        </div>
                      </Modal.Body>
                    }

            </Modal>
           <Container >
            <Row>
            <Card className="text-center m-2 w-50" style={itemStyle}>
                <Card.Header >Font 
                <button onClick={() => this.editForm("Font") } className="btn btn-outline-dark btn-sm float-right"> <i className='fas fa-edit'></i> </button>
                </Card.Header>
                    <Card.Body>
                        {/* if Font is not the section to edit render the database info and a button to edit*/}

                        {this.state.sectionEdit !== "Font" ?
                          <p style={{margin: "0", padding: "0.8em"}}>{this.state.customizeInfo[5][1]}</p>
                            :   
                            <form className="form-inline">
                            {/* choose font and submit to change font */}
                            <div style= {{float: 'left'}}>
                                <select id="lang" onChange={this.change.bind(this)} value={this.state.value}>
                                    {/* <option value="Selected">{this.state.customizeInfo[5][1]}</option> */}
                                    
                                    {/* dropdown menu options */}
                                    <option value="Oswald">{this.state.fonts[0]}</option>
                                    <option value="Raleway">{this.state.fonts[1]}</option>
                                    <option value="Open Sans">{this.state.fonts[2]}</option>
                                    <option value="Lato">{this.state.fonts[3]}</option>
                                    <option value="Pt Sans">{this.state.fonts[4]}</option>
                                    <option value="Lora">{this.state.fonts[5]}</option>
                                    <option value="Montserrat">{this.state.fonts[6]}</option>
                                    <option value="Playfair Display">{this.state.fonts[7]}</option>
                                    <option value="Benchnine">{this.state.fonts[8]}</option>
                                    <option value="Merriweather">{this.state.fonts[9]}</option>


                                </select>     
                            </div>
                            <br></br>
                            <div className="row m-2">
                                        <button  className="btn btn-primary" style = {{backgroundColor: '#0B658A', border: '#0B658A'}}>Submit</button>
                            </div>
                            <button onClick={() => this.editForm("")} type="button" className="btn btn-outline-danger ml-4" >Cancel</button>
                                </form>
                        }
                    </Card.Body>
                </Card>
                  <Card className="text-center m-2 w-45" style={itemStyle}>
                    <Card.Header >Logo
                      <button  onClick={() => this.editForm("Logo") } className="btn btn-outline-dark btn-sm float-right"> <i className='fas fa-edit'></i> </button>
                    </Card.Header>
                    <Card.Body >
                      <div className = "p-3">
                        {this.state.sectionEdit !== "Logo" ? 
                            <img src={this.props.logo}  width="auto" height="45px" alt="waiter" /> 
                             :
                             <Container>
                            <div className="input-group">
                              <div className="custom-file">
                                <input
                                  onChange={this.onChangeFile}
                                  type="file"
                                  className="custom-file-input"
                                  id="inputGroupFile01"
                                  aria-describedby="inputGroupFileAddon01"
                                />
                                <label className="custom-file-label" htmlFor="inputGroupFile01">
                                  {this.state.fileName}
                                </label>

                              </div>
                            </div>
                            <br/>
                            <div className="row m-2">
                               <button  className="btn btn-primary" style = {{backgroundColor: '#0B658A', border: '#0B658A'}}>Submit</button>
                            <button onClick={() => this.editForm("")} type="button" className="btn btn-outline-danger ml-4" >Cancel</button>
                             </div>
                         </Container>
                        }
                    </div> 
                    </Card.Body>
                   </Card>
                </Row>

                <Row>
                  <Card className="text-center m-2 w-20" style={itemStyle} >
                    <Card.Header onClick={this.handleModalShow}>Primary
                      <button onClick={() => this.editForm("Primary") } className="btn btn-outline-dark btn-sm float-right ml-4"> <i className='fas fa-edit'></i> </button>
                    </Card.Header>
                    <Card.Body style={{backgroundColor:this.state.primary}}>

                    </Card.Body>
                   </Card>
                  <Card className="text-center m-2 w-20" style={itemStyle}>
                    <Card.Header onClick={this.handleModalShow}>Secondary
                      <button onClick={() => this.editForm("Secondary") } className="btn btn-outline-dark btn-sm float-right ml-4"> <i className='fas fa-edit'></i> </button>
                    </Card.Header>
                    <Card.Body style={{backgroundColor:this.state.secondary, minHeight:'10vh'}}>

                    </Card.Body>
                   </Card>
    
                   <Card className="text-center m-2 w-20" style={itemStyle}>
                    <Card.Header onClick={this.handleModalShow}>Tertiary
                      <button  onClick={() => this.editForm("Tertiary") } className="btn btn-outline-dark btn-sm float-right ml-4"> <i className='fas fa-edit'></i> </button>
                    </Card.Header>
                    <Card.Body style={{backgroundColor:this.state.tertiary }}>

                    </Card.Body>
                   </Card>
                   <Card className="text-center m-2 w-20" style={itemStyle}>
                    <Card.Header onClick={this.handleModalShow}>Font Color
                      <button  onClick={() => this.editForm("Font Color") } className="btn btn-outline-dark btn-sm float-right ml-4"> <i className='fas fa-edit'></i> </button>
                    </Card.Header>
                    <Card.Body style={{backgroundColor:this.state.font_color }}>

                    </Card.Body>
                   </Card>
                  </Row>
                </Container>
            </>
        )

}
    render() {
        const {customizeInfo } = this.state;
        const resturantInfo = this.props.info;
        console.log(resturantInfo);
        //put resturant info into an array
        Object.keys(resturantInfo).forEach(function(key) {
            customizeInfo.push([key ,resturantInfo[key]]);
        });
        return (
            <Container>
                <div style={backgroundStyle}>
                <h2 style ={menuHeaderStyle}>
                  Customize
                </h2>
                    <Container fluid style={{'minHeight': '70vh',   'minWidth': '100%'}}>
                        <div className="d-flex flex-wrap">
                            {this.renderInfo()}
                        </div>
                    </Container>
                </div>
            </Container>
        );
    }
}

const backgroundStyle = {
  'backgroundColor': '#f1f1f1',
  'minWidth': '70vw'
}

const itemStyle = {
    'borderBottom': 'grey solid 1px',

};
const menuHeaderStyle = {
  'backgroundColor': '#102644',
  'color': '#ffffff',
  'fontFamily': 'Kefa',
  'textAlign' : 'center',
  'height':'54px'
};
const modalImageStyle = {
  'max-width': '200px',
  'max-height': '200px'
}
export default Customize;
