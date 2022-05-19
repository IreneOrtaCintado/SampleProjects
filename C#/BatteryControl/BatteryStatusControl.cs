using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace BatteryControl
{
    [ToolboxBitmap(typeof(BatteryStatusControl), "icon_battery.png")]
    public partial class BatteryStatusControl : UserControl
    {
        private int nivel;
        private bool cargando;
        private bool agotada;
        private Color chargeColor;
        private Color lowChargeColor;
        
        //  control
        private bool mouseDrag;                                 //  tracks mouse down (true) and up (false) events
        private int positionBaterryFull, batteryFullHeight;     //  track battery charge rectangle dimentions
        private Font percentageFont;                            //  font for the percentage
        private int recordedWidth;                              //  to keep a record of the current width in case the component changes size with font changes

        //  CUSTOM EVENT
        //  inner class to create custom events
        public class Arguments : EventArgs
        {
            int batteryLevel;
            public Arguments(int batteryLevel)
            {
                this.batteryLevel = batteryLevel;
            }

            public int BatteryLevel
            {
                get { return this.batteryLevel; }
            }
        }
        //  delegate method end event
        public delegate void NivelCambiadoEvent(Object sender, Arguments args);
        public event NivelCambiadoEvent NivelCambiado;

        //  COSNTRUCTOR
        public BatteryStatusControl()
        {
            InitializeComponent();

            //  component size
            recordedWidth = 100;
            this.Width = 100;
            this.Height = (this.Width * 2);

            //  colors
            chargeColor = Color.Green;
            lowChargeColor = Color.Red;
            //  values
            nivel = 50;
            //  mouse
            mouseDrag = false;
            // Font
            this.Font = new Font("Arial", 12, FontStyle.Bold);  // default Font
            float fontSize = (float)(this.Width * (this.Font.Size / 100.0));
            percentageFont = new Font(this.Font.FontFamily, fontSize, FontStyle.Bold);
        }

        //  PROPERTIES
        [Description("Nivel de carga."), DefaultValue(50)]
        public int Nivel
        {
            get { return nivel; }
            set
            {
                //  limit the values between 0 and 100
                if (value > 100) nivel = 100;
                else if (value < 0) nivel = 0;
                else nivel = value;

                //  update if the battery is empty
                if (nivel == 0) agotada = true;
                else agotada = false;

                Invalidate();
                var eventBatteryChange = this.NivelCambiado;
                if (eventBatteryChange != null) eventBatteryChange(this, new Arguments(value));
            }
        }

        [Description("Batería cargando."), DefaultValue(false)]
        public bool Cargando
        {
            get { return cargando; }
            set
            {
                cargando = value;
                Invalidate();
            }
        }

        [Description("Batería agotada.")]
        public bool Agotada
        {
            get { return agotada; }
        }

        //  PAINT
        protected override void OnPaint(PaintEventArgs e)
        {
            base.OnPaint(e);
            Graphics g = e.Graphics;    //  g = canvas

            this.DoubleBuffered = true;     //  avoids flickering

            recordedWidth = this.Width;         //  keeps track of the size

            //  get canvas dimentions
            int mainWidth = this.Width;
            int mainHeight = this.Height;

            //  set image dimentions
            int imageWidth = mainWidth;
            int imageHeight = (int)(mainHeight * 0.88);

            //  full charge rectangle dimentions and position
            double horizontalMarginChargeRect = 0.105;  // % of component width
            int postionVertical = (int)(imageHeight * 0.091);
            int postionHorizontal = (int)(imageWidth * horizontalMarginChargeRect);
            int rectangleWidth = (int)(imageWidth - (postionHorizontal * 2));
            int maxRectangleHeight = (int)(imageHeight * 0.854);

            //  set vertical positions of battery charge to control it with the mouse
            positionBaterryFull = postionVertical;
            batteryFullHeight = maxRectangleHeight;

            //  resize rectangle to battery level
            int resizedRectangleHeight = (int)(maxRectangleHeight * (nivel / 100.0));
            int resizedPositionVertical = postionVertical + maxRectangleHeight - resizedRectangleHeight;

            //  pick color
            Brush chargeBrush;
            if (nivel <= 10) chargeBrush = new SolidBrush(lowChargeColor);
            else chargeBrush = new SolidBrush(chargeColor);

            // paint battery level
            g.FillRectangle(chargeBrush, postionHorizontal, resizedPositionVertical, rectangleWidth, resizedRectangleHeight);

            //  pick image
            if (cargando) g.DrawImage(BatteryControl.Properties.Resources.carga, 0, 0, imageWidth, imageHeight);
            else if (nivel == 0) g.DrawImage(BatteryControl.Properties.Resources.agotada, 0, 0, imageWidth, imageHeight);
            else g.DrawImage(BatteryControl.Properties.Resources._base, 0, 0, imageWidth, imageHeight);

            //  height of percentage string rectangle
            int labelHeight = (int)(mainHeight * 0.12);

            // change font
            float fontSize = (float)(this.Width * (this.Font.Size / 100.0));
            percentageFont = new Font(this.Font.FontFamily, fontSize, FontStyle.Bold);

            // format of percentage string
            StringFormat drawFormat = new StringFormat();
            drawFormat.Alignment = StringAlignment.Center;

            //  paint percentage
            g.DrawString(nivel + "%", percentageFont, Brushes.Black, new Rectangle(new Point(0, imageHeight), new Size(this.Width, labelHeight)), drawFormat);
        }


        //  RESIZE EVENT
        private void BatteryStatusControl_Layout(object sender, LayoutEventArgs e)
        {
            if (e.AffectedProperty == "Bounds")  //  change in control bounds
            {
                this.Height = this.Width * 2;
                Invalidate();   //  repaint: calls OnPaint
            }
        }

        //  MOUSE DRAG: change level
        private void BatteryStatusControl_MouseDown(object sender, MouseEventArgs e)
        {
            mouseDrag = true;
        }
        private void BatteryStatusControl_MouseMove(object sender, MouseEventArgs e)
        {
            if (mouseDrag)
            {
                Boolean mouseMoreThanMax = e.Location.Y <= positionBaterryFull;
                Boolean mouseLessThanEmpty = e.Location.Y > (positionBaterryFull + batteryFullHeight);
                if (mouseMoreThanMax) Nivel = 100;
                else if (mouseLessThanEmpty) Nivel = 0;
                else Nivel = 100 - (100 * (e.Location.Y - positionBaterryFull) / batteryFullHeight);
            }
        }

        private void BatteryStatusControl_MouseUp(object sender, MouseEventArgs e)
        {
            mouseDrag = false;
        }

        //  MOUSE DOUBLE CLICK: change charge status
        private void BatteryStatusControl_MouseDoubleClick(object sender, MouseEventArgs e)
        {
            if (cargando) Cargando = false;
            else Cargando = true;
        }

        //  FONT CHANGED
        private void BatteryStatusControl_FontChanged(object sender, EventArgs e)
        {
            //  avoids autoresize when the font is changed
            this.Width = recordedWidth;
            this.Height = recordedWidth * 2;
            //  keeps the size of the font to a range that works with the component
            if (this.Font.Size > 16) this.Font = new Font(this.Font.FontFamily,16);
            else if (this.Font.Size < 8) this.Font = new Font(this.Font.FontFamily, 8);
            Invalidate();
        }


    }
}


