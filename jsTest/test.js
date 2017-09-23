function reWrite(num)
{
  if (document.getElementById)
  {
    if (num==0)
    {
      document.getElementById("t1").textContent="spanish!";
    }
    else
    {
      document.getElementById("t1").innerHTML="French";
    }  
  }
}